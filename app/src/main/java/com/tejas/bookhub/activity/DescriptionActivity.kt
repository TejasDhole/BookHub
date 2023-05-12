package com.tejas.bookhub.activity



import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import android.content.Intent
import android.os.AsyncTask
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import com.tejas.bookhub.R
import com.tejas.bookhub.database.BookDatabase
import com.tejas.bookhub.database.BookEntity
import com.tejas.bookhub.util.ConnectionManager
import org.json.JSONObject
import kotlin.Exception






class DescriptionActivity : AppCompatActivity() {

    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var toolbar: Toolbar
    lateinit var txtBookName:TextView
    lateinit var txtBookAuthor:TextView
    lateinit var txtBookImage:ImageView
    lateinit var txtBookDecs:TextView
    lateinit var addFavBtn:Button
    lateinit var txtBookPrice:TextView
    lateinit var txtBookRating: TextView

    var bookId: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookImage = findViewById(R.id.bookimg)

        addFavBtn = findViewById(R.id.btnAddFav)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")

        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "some unexpected error occurred",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (bookId == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "some unexpected error occurred",
                Toast.LENGTH_SHORT
            ).show()
        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)

        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {
            val jsonRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonParams,Response.Listener {


                        try {

                            val success = it.getBoolean("success")

                            if (success) {
                                val bookJsonObject = it.getJSONObject("book_data")
                                progressLayout.visibility = View.GONE

                                val bookImageUrl = bookJsonObject.getString("image")
                                Picasso.get().load(bookJsonObject.getString("image")).error(R.drawable.default_book_cover).into(txtBookImage)
                                txtBookName.text = bookJsonObject.getString("name")
                                txtBookAuthor.text = bookJsonObject.getString("author")
                                txtBookPrice.text = bookJsonObject.getString("price")
                                txtBookRating.text = bookJsonObject.getString("rating")
                                txtBookDecs.text = bookJsonObject.getString("description")

                                val bookEntity = BookEntity(
                                    bookId?.toInt() as Int,
                                    txtBookName.text.toString(),
                                    txtBookAuthor.text.toString(),
                                    txtBookPrice.text.toString(),
                                    txtBookRating.text.toString(),
                                    txtBookDecs.text.toString(),
                                    bookImageUrl
                                )

                                val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                                val isFav = checkFav.get()

                                if(isFav){
                                    addFavBtn.text = "Remove From Favourites"
                                    val FavColor = ContextCompat.getColor(applicationContext,R.color.colorFavourite)
                                    addFavBtn.setBackgroundColor(FavColor)
                                }
                                else{
                                    addFavBtn.text = "Add From Favourites"
                                    val noFavColor = ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                    addFavBtn.setBackgroundColor(noFavColor)

                                }

                                addFavBtn.setOnClickListener{

                                    if(!DBAsyncTask(applicationContext,bookEntity,1).execute().get()){
                                        val async = DBAsyncTask(applicationContext,bookEntity,2).execute()
                                        val result = async.get()
                                        if(result){
                                            Toast.makeText(
                                                this@DescriptionActivity,
                                                "Book added to favourites",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            addFavBtn.text = "Remove From Favourties"
                                            val favColor = ContextCompat.getColor(applicationContext,R.color.colorFavourite)
                                            addFavBtn.setBackgroundColor(favColor)

                                        }
                                        else{
                                            Toast.makeText(
                                                this@DescriptionActivity,
                                                "some error Occurred!!!!",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }
                                    }else{
                                        val async = DBAsyncTask(applicationContext,bookEntity,3).execute()
                                        val result = async.get()

                                        if(result){
                                            Toast.makeText(
                                                this@DescriptionActivity,
                                                "Book removed from favourites",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            addFavBtn.text = "Add to Favourties"
                                            val noFavColor = ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                            addFavBtn.setBackgroundColor(noFavColor)

                                        }
                                        else{
                                            Toast.makeText(
                                                this@DescriptionActivity,
                                                "some error Occurred!!!!",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }

                                    }
                                }

                            } else {
                                 Toast.makeText(
                                    this@DescriptionActivity,
                                    "some error Occurred!!!!",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }


                        } catch (e: Exception) {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "some unexpected error occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    Response.ErrorListener {

                        Toast.makeText(
                            this@DescriptionActivity,
                            "Volley error occurred!!!! $it",
                            Toast.LENGTH_SHORT
                        ).show()


                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "9bf534118365f1"
                        return headers
                    }
                }
            queue.add(jsonRequest)
        }else {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }

            dialog.setNegativeButton("Exit") {text, listener ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }

    class DBAsyncTask(val context : Context, val  bookEntity: BookEntity, val mode:Int) : AsyncTask<Void,Void,Boolean>(){

        val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {



            when(mode){

                1->{//check DB if the book is favourite or not

                    val book:BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book !=null

                }

                2-> {

                    //save teh book into DB as favourite
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true

                }

                3-> {
                    //remove the favourite book
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true

                }
            }

            return false
        }

    }
}
