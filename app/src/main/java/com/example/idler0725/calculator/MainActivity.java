package com.example.idler0725.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //変数宣言＆初期化
    TextView old_formula;                        //履歴フィールド
    EditText current_formula;                   //入力フィールド
    int recentOperation = R.id.button_Equal; //計算記号格納
    double result;                              //計算結果格納
    boolean operationKeyPushed;               //計算記号が押されているか

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        old_formula = (TextView) findViewById(R.id.old_formula);
        current_formula = (EditText) findViewById(R.id.current_formula);

        //数字を取得
        findViewById(R.id.button_1).setOnClickListener(onClickNumber);
        findViewById(R.id.button_2).setOnClickListener(onClickNumber);
        findViewById(R.id.button_3).setOnClickListener(onClickNumber);
        findViewById(R.id.button_4).setOnClickListener(onClickNumber);
        findViewById(R.id.button_5).setOnClickListener(onClickNumber);
        findViewById(R.id.button_6).setOnClickListener(onClickNumber);
        findViewById(R.id.button_7).setOnClickListener(onClickNumber);
        findViewById(R.id.button_8).setOnClickListener(onClickNumber);
        findViewById(R.id.button_9).setOnClickListener(onClickNumber);
        findViewById(R.id.button_0).setOnClickListener(onClickNumber);
        findViewById(R.id.button_00).setOnClickListener(onClickNumber);
        findViewById(R.id.button_Dot).setOnClickListener(onClickNumber);

        //記号を取得
        findViewById(R.id.button_C).setOnClickListener(onClickClear);
        findViewById(R.id.button_CA).setOnClickListener(onClickClearAll);

        findViewById(R.id.button_Addition).setOnClickListener(onClickOperation);
        findViewById(R.id.button_Subtraction).setOnClickListener(onClickOperation);
        findViewById(R.id.button_Multiplication).setOnClickListener(onClickOperation);
        findViewById(R.id.button_Division).setOnClickListener(onClickOperation);
        findViewById(R.id.button_Equal).setOnClickListener(onClickOperation);
    }

    //「C」が押された時の処理
    View.OnClickListener onClickClear = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //入力フィールドのみ消去
            current_formula.setText("");
        }
    };

    //「CA」が押された時の処理
    View.OnClickListener onClickClearAll = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //入力状態を消去
            recentOperation = R.id.button_Equal;
            result = 0;
            operationKeyPushed = false;
            old_formula.setText("");
            current_formula.setText("");
        }
    };

    //数字が押された時の処理
    View.OnClickListener onClickNumber = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;      //数字を取得
            //直前に記号が押されていた場合
            if (operationKeyPushed == true) {
                //新しく入力フォームに押された数字を表示
                current_formula.setText(button.getText());
            }
            //それ以外の場合
            else {
                //既存の数字の後に押された数字を追加
                current_formula.append(button.getText());
            }
            //記号が押されていない状態に
            operationKeyPushed = false;
        }
    };

    //記号が押された時の処理
    View.OnClickListener onClickOperation = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button operationButton = (Button) view;                                         //記号を取得
            double value = Double.parseDouble(current_formula.getText().toString());    //記号が押される前の数値を取得
            //一個前に押された記号が「＝」の場合
            if (recentOperation == R.id.button_Equal) {
                result = value;
            }
            //それ以外の場合
            else {
                //計算結果を算出
                result = calculation(recentOperation, result, value);
                //計算結果を入力フィールドに表示
                current_formula.setText(String.valueOf(result));
                //履歴フィールドを更新
                old_formula.setText(String.valueOf(result) + " " + operationButton.getText());
                //入力フィールドを消去
                current_formula.setText("");
            }
            //押された記号を更新
            recentOperation = operationButton.getId();
            //記号が押されている状態に
            operationKeyPushed = true;
        }
    };

    //計算メソッド
    double calculation(int operation, double value1, double value2) {
        //記号によって分岐
        switch (operation) {
            //足し算
            case R.id.button_Addition:
                return value1 + value2;
            //引き算
            case R.id.button_Subtraction:
                return value1 - value2;
            //掛け算
            case R.id.button_Multiplication:
                return value1 * value2;
            //割り算
            case R.id.button_Division:
                return value1 / value2;
            //平方根
            case R.id.button_sRoot:
                return value1 / value2;
            default:
                return value1;
        }
    }
}

