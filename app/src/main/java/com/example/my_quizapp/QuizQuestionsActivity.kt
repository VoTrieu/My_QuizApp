package com.example.my_quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.my_quizapp.databinding.ActivityQuizQuestionsBinding

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var questionsList:ArrayList<Question>? = null
    private lateinit var binding: ActivityQuizQuestionsBinding
    private var currentPosition:Int = 1
    private var correctAnswers:Int = 0
    private var userName:String? = null
    private var selectedOptionPosition:Int = 0
    private var options = ArrayList<TextView>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_quiz_questions)
        binding = ActivityQuizQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.userName = intent.getStringExtra(Constants.USER_NAME)

        questionsList = Constants.getQuestions()

        setQuestion()
        binding.tvOptionOne.setOnClickListener(this)
        binding.tvOptionTwo.setOnClickListener(this)
        binding.tvOptionThree.setOnClickListener(this)
        binding.tvOptionFour.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)


    }

    private fun setQuestion(){
        val question:Question = this.questionsList!![this.currentPosition - 1]
        binding.btnSubmit.text = "SUBMIT"
        binding.tvQuestion.text = question.question
        binding.progressBar.progress = this.currentPosition
        binding.tvProgress.text = "${this.currentPosition} / ${this.questionsList!!.size}"
        binding.ivImage.setImageResource(question.image)
        binding.tvOptionOne.text = question.optionOne
        binding.tvOptionTwo.text = question.optionTwo
        binding.tvOptionThree.text = question.optionThree
        binding.tvOptionFour.text = question.optionFour
        setDefaultOptionsView()
    }

    private fun setDefaultOptionsView(){
        this.options.clear()
        binding.tvOptionOne?.let {
            this.options.add(0,it)
        }
        binding.tvOptionTwo?.let {
            this.options.add(1,it)
        }
        binding.tvOptionThree?.let {
            this.options.add(2,it)
        }
        binding. tvOptionFour?.let {
            this.options.add(3,it)
        }

        for (option in options){
            option.setTextColor(Color.parseColor("#7A8089"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this@QuizQuestionsActivity,
                R.drawable.default_option_border_bg
            )
        }
    }

    private fun selectedOptionView(tv: TextView, selectedOptionNum:Int){
        this.setDefaultOptionsView()
        this.selectedOptionPosition = selectedOptionNum
        tv.setTextColor(
            Color.parseColor("#363A43")
        )
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this@QuizQuestionsActivity,
            R.drawable.selected_option_border_bg
        )
    }

    private fun setAnswersView(answer:Int){
        val currentQuestion = this.questionsList?.get(this.currentPosition - 1)
        val selectedOption = this.options[answer - 1]
        if(answer != currentQuestion!!.correctAnswer){
            selectedOption.background =  ContextCompat.getDrawable(
                this@QuizQuestionsActivity,
                R.drawable.wrong_option_border_bg)
        }else{
            this.correctAnswers++
        }
        val correctOption = this.options[currentQuestion.correctAnswer - 1]
        correctOption.background =  ContextCompat.getDrawable(
            this@QuizQuestionsActivity,
            R.drawable.correct_option_border_bg)

        this.currentPosition++
        binding.btnSubmit.text = if (this.currentPosition <= this.questionsList!!.size)  "NEXT" else "FINISH"

    }

    override fun onClick(view: View?) {
        when (view?.id){
            R.id.tv_option_one -> {
                binding.tvOptionOne?.let {
                    selectedOptionView(it, 1)
                }
            }
            R.id.tv_option_two -> {
                binding.tvOptionTwo?.let {
                    selectedOptionView(it, 2)
                }
            }
            R.id.tv_option_three -> {
                binding.tvOptionThree?.let {
                    selectedOptionView(it, 3)
                }
            }
            R.id.tv_option_four -> {
                binding.tvOptionFour?.let {
                    selectedOptionView(it, 4)
                }
            }
            R.id.btn_submit ->{
                when(true){
                    (this.selectedOptionPosition == 0) ->{
                        Toast.makeText(this, "Please select your answer!", Toast.LENGTH_LONG).show()
                    }

                    (binding.btnSubmit.text == "SUBMIT") -> {
                        this.setAnswersView(selectedOptionPosition)
                    }
                    else -> {
                        if(this.currentPosition <= this.questionsList!!.size){
                            this.setQuestion()
                        }else{
                            val intent = Intent(this@QuizQuestionsActivity, ResultActivity::class.java)
                            intent.putExtra(Constants.USER_NAME, this.userName)
                            intent.putExtra(Constants.CORRECT_ANSWER, this.correctAnswers)
                            intent.putExtra(Constants.TOTAL_QUESTIONS, this.questionsList!!.size)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }
}