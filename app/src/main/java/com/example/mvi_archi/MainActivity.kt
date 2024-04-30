package com.example.mvi_archi

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var numberTextView:TextView;
    lateinit var numberButton:Button;
    private val viewModel:AddNumberViewModel by lazy<AddNumberViewModel> {
        ViewModelProvider(this).get(AddNumberViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        numberTextView = findViewById<TextView>(R.id.textView);
        numberButton = findViewById<Button>(R.id.button);

        //Send event
        numberButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.intentChannel.send(MainIntent.AddNumber)
            }
        }

        //Render UI
        renderStateView()

    }
    private fun renderStateView(){
        lifecycleScope.launch {
            viewModel.state.collect{
                when(it){
                    is MainViewState.Idle -> numberTextView.text = "0"
                    is MainViewState.AddNumber -> numberTextView.text = it.number.toString()
                    is MainViewState.Error -> numberTextView.text = it.error
                }
            }
        }
    }
}