package com.android.geoquiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {


    private static final String TAG = "QuizActivity";
    public static final String KEY_INDEX = "index";
    public static final int REQUEST_CODE_CHEAT = 0;
    private int cheatCount = 0;
    private int mCurrentIndex = 0;
    private boolean mIsCheater;
    private boolean isAnswered;
    int correctAnswers = 0, incorrectAnswers = 0;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private Button mNextButton;
    private TextView mAnswerTextView;
    private TextView mQuestionTextView;
    private TextView scoreTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_britain, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreTextView = findViewById(R.id.score);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
//            mQuestionBank[mCurrentIndex].setAnswered(savedInstanceState.getBoolean("isAnswered", false));
            mQuestionBank = (Question[]) savedInstanceState.getParcelableArray("array");
            mIsCheater = savedInstanceState.getBoolean("isCheater");
            correctAnswers = savedInstanceState.getInt("correct");
            incorrectAnswers = savedInstanceState.getInt("incorrect");
            scoreTextView.setText(savedInstanceState.getCharSequence("score"));
            cheatCount = savedInstanceState.getInt("cheatCount");
        }

        mAnswerTextView = findViewById(R.id.answer_field);


        mQuestionTextView = findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                mQuestionBank[mCurrentIndex].setAnswered(true);
                setButtonsDisabled();
            }
        });
        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                mQuestionBank[mCurrentIndex].setAnswered(true);
                setButtonsDisabled();
            }
        });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].ismAnswerTrue();
//                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                intent.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, answerIsTrue);
                intent.putExtra("cheatCount", cheatCount);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });
        updateQuestion();
    }
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getmTextResId();
        mQuestionTextView.setText(question);
        System.out.println("update question: isAnswered = " + mQuestionBank[mCurrentIndex].isAnswered() );
        setButtonsDisabled();
    }

    private void setButtonsDisabled() {
        isAnswered = mQuestionBank[mCurrentIndex].isAnswered();
        if (isAnswered) {
            mAnswerTextView.setText("Answered");
        } else
            mAnswerTextView.setText("");
        mTrueButton.setEnabled(!isAnswered);
        mFalseButton.setEnabled(!isAnswered);
        mCheatButton.setEnabled(cheatCount < 3);
//        mCheatButton.setEnabled(!isAnswered);
//        mNextButton.setEnabled(!(correctAnswers + incorrectAnswers == mQuestionBank.length));
    }

    private void checkAnswer (boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].ismAnswerTrue();
        int messageResId = 0;
        if(mIsCheater) {
            messageResId = R.string.judgment_toast;
            incorrectAnswers++;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                correctAnswers++;
            } else {
                messageResId = R.string.incorrect_toast;
                incorrectAnswers++;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        System.out.println("correct: " + correctAnswers + " incorrect: " + incorrectAnswers);
        if (correctAnswers + incorrectAnswers == mQuestionBank.length) {
            scoreTextView.setText("You scored " + correctAnswers + "/" + mQuestionBank.length + " correct answers\nGame over");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSavedInstanceState");
        outState.putInt(KEY_INDEX, mCurrentIndex);
        outState.putParcelableArray("array", mQuestionBank);
        outState.putBoolean("isCheater", mIsCheater);
        outState.putInt("correct", correctAnswers);
        outState.putInt("incorrect", incorrectAnswers);
        outState.putCharSequence("score", scoreTextView.getText());
        outState.putInt("cheatCount", cheatCount);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null)
                return;
            mIsCheater = CheatActivity.wasAnswerShown(data);
            cheatCount++;
            setButtonsDisabled();
        }
    }

    public int getCheatCount() {
        return cheatCount;
    }
}
