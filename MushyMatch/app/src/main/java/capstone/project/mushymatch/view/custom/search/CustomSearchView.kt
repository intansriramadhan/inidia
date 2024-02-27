package capstone.project.mushymatch.view.custom.search

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import capstone.project.mushymatch.view.home.HomePageActivity

class CustomSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint()
    private val shadowPaint = Paint()
    private val cornerRadius = 5f
    private val rect = RectF()
    private var searchIcon: Drawable? = null
    private var searchQuery: String = ""

    init {
        backgroundPaint.color = Color.WHITE
        backgroundPaint.isAntiAlias = true

        // Menerapkan efek bayangan
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        shadowPaint.color = Color.GRAY
        shadowPaint.setShadowLayer(20f, 0f, 8f, Color.DKGRAY)

        // Mengatur padding
        setPadding(16, 16, 16, 16)

        // Mengatur warna teks
        setTextColor(Color.BLACK)

        // Menggunakan listener TextWatcher untuk mendeteksi perubahan teks di EditText
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Implementasikan logika pencarian di sini
                val query = s.toString()
                // Panggil method atau fungsi yang akan meng-handle logika pencarian
                performSearch(query)
            }
        })

        // Mengambil ikon search dari atribut drawableLeft
        searchIcon = compoundDrawablesRelative[0]
        searchIcon?.setBounds(0, 0, searchIcon?.intrinsicWidth ?: 0, searchIcon?.intrinsicHeight ?: 0)
        setCompoundDrawablesRelative(searchIcon, null, null, null)
    }

    override fun onDraw(canvas: Canvas) {
        val viewHeight = height.toFloat()
        val viewWidth = width.toFloat()
        rect.set(0f, 0f, viewWidth, viewHeight)

        // Menggambar efek bayangan
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, shadowPaint)

        // Menggambar latar belakang dengan sudut yang dibulatkan
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, backgroundPaint)

        super.onDraw(canvas)
    }

    private fun performSearch(query: String) {
        searchQuery = query
        // Panggil method atau fungsi yang akan meng-handle logika pencarian di HomePageActivity
        (context as? HomePageActivity)?.performSearch(query)
    }

}
