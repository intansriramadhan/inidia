package capstone.project.mushymatch.view.scan

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import capstone.project.mushymatch.databinding.ActivityDetectionBinding
import capstone.project.mushymatch.util.uriToFile
import capstone.project.mushymatch.view.about.MushroomInformationActivity
import org.tensorflow.lite.Interpreter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class DetectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetectionBinding
    private lateinit var interpreter: Interpreter
    private var classificationResult = 0
    private val labelList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        showIdentificationResult(false)

        binding.importImage.setOnClickListener{
            startGallery()
        }

        binding.captureImage.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            } else {
                Toast.makeText(this, "Camera tidak tersedia", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnMoreInformation.setOnClickListener {
            val intent = Intent(this@DetectionActivity, MushroomInformationActivity::class.java)
            intent.putExtra("label", classificationResult)
            startActivity(intent)

        }

        binding.back.setOnClickListener {
            finish()
        }


    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val file = createImageFile()
            saveBitmapToFile(imageBitmap, file)
            val selectedImageUri = Uri.fromFile(file)
            openResultActivity(selectedImageUri)
        }
    }

    private var getFile: File? = null

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile

            val savedUri = Uri.fromFile(myFile)

            // Load the machine learning model
            interpreter = loadModelFile()

            val bitmap = BitmapFactory.decodeFile(savedUri.path)
            binding.previewImage.setImageBitmap(bitmap)
            processImage(bitmap)

        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    private fun saveBitmapToFile(bitmap: Bitmap, file: File) {
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    }

    private fun openResultActivity(imageUri: Uri) {
        // Load the machine learning model
        interpreter = loadModelFile()

        val bitmap = BitmapFactory.decodeFile(imageUri.path)
        val croppedBitmap = cropToSquare(bitmap)
        binding.previewImage.setImageBitmap(croppedBitmap)
        processImage(croppedBitmap)

//        val intent = Intent(this, ResultActivity::class.java)
//        val bundle = Bundle()
//        bundle.putParcelable("selected_image", imageUri)
//        intent.putExtra("bundle", bundle)
//        startActivity(intent)
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(baseContext, permission) == PackageManager.PERMISSION_GRANTED
    }

    //ml
    private fun loadModelFile(): Interpreter {
        val modelFileDescriptor = assets.openFd("model.tflite")
        val inputStream = FileInputStream(modelFileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = modelFileDescriptor.startOffset
        val declaredLength = modelFileDescriptor.declaredLength
        val mappedByteBuffer: MappedByteBuffer =
            fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        val interpreter = Interpreter(mappedByteBuffer)
        inputStream.close()
        return interpreter
    }

    private fun preprocessImage(image: Bitmap): ByteBuffer {
        val inputSize = 224
        val resizedImage = Bitmap.createScaledBitmap(image, inputSize, inputSize, false)
        val inputBuffer = ByteBuffer.allocateDirect(inputSize * inputSize * 3 * 4)

        inputBuffer.order(ByteOrder.nativeOrder())
        inputBuffer.rewind()

        val pixels = IntArray(inputSize * inputSize)
        resizedImage.getPixels(pixels, 0, inputSize, 0, 0, inputSize, inputSize)

        for (pixelValue in pixels) {
            val r = (pixelValue shr 16 and 0xFF) / 255.0f
            val g = (pixelValue shr 8 and 0xFF) / 255.0f
            val b = (pixelValue and 0xFF) / 255.0f

            inputBuffer.putFloat(r)
            inputBuffer.putFloat(g)
            inputBuffer.putFloat(b)
        }

        return inputBuffer
    }

    private fun runInference(inputBuffer: ByteBuffer): Pair<Int, Float> {
        val outputShape = interpreter.getOutputTensor(0).shape()
        val outputSize = outputShape[1]

        val outputBuffer = ByteBuffer.allocateDirect(outputSize * 4)
        outputBuffer.order(ByteOrder.nativeOrder())
        outputBuffer.rewind()

        interpreter.run(inputBuffer, outputBuffer)

        return processOutput(outputBuffer)
    }

    private fun processOutput(outputBuffer: ByteBuffer): Pair<Int, Float> {
        val outputFloatArray = FloatArray(outputBuffer.capacity() / 4)
        outputBuffer.rewind()
        outputBuffer.asFloatBuffer().get(outputFloatArray)

        var maxIndex = 0
        var maxValue = outputFloatArray[0]
        for (i in 1 until outputFloatArray.size) {
            if (outputFloatArray[i] > maxValue) {
                maxValue = outputFloatArray[i]
                maxIndex = i
            }
        }

        return Pair(maxIndex, maxValue)
    }

    @SuppressLint("SetTextI18n")
    private fun processImage(image: Bitmap) {
        val inputBuffer = preprocessImage(image)
        val (maxIndex, maxValue) = runInference(inputBuffer)

        // Load the label list if it's empty
        if (labelList.isEmpty()) {
            loadLabelList()
        }

        this.classificationResult = maxIndex + 1
        val className = labelList[maxIndex]
        val accuracyPercentage = (maxValue * 100).toInt() // Convert accuracy to percentage

        binding.tvResultName.text = className
        binding.tvResultAccuracy.text = "Accuracy: $accuracyPercentage%"

        showIdentificationResult(true)
    }

    private fun loadLabelList() {
        val labelFilename = "labels.txt"
        try {
            val labels = assets.open(labelFilename).bufferedReader().useLines { it.toList() }
            labelList.clear()
            labelList.addAll(labels)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun cropToSquare(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val size = if (width < height) width else height

        val x = (width - size) / 2
        val y = (height - size) / 2

        val matrix = Matrix()

        return Bitmap.createBitmap(bitmap, x, y, size, size, matrix, true)
    }

    private fun showIdentificationResult(state: Boolean) {
        if (state) {
            binding.contentResult.visibility = View.VISIBLE
            binding.contentBegin.visibility = View.GONE
        } else {
            binding.contentResult.visibility = View.GONE
            binding.contentBegin.visibility = View.VISIBLE
        }
    }


    companion object {
        internal val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        internal const val REQUEST_CODE_PERMISSIONS = 10
        private const val REQUEST_IMAGE_CAPTURE = 1001
        private const val TAG = "DetectionActivity"
    }
}