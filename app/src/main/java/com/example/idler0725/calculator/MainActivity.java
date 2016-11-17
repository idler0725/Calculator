package com.example.idler0725.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import static java.lang.Math.sqrt;

public class MainActivity extends AppCompatActivity {
    //変数宣言
    TextView oldFormula;           //履歴テキスト
    TextView currentFormula;      //入力テキスト
    int recentOperation;          //演算記号格納
    double result;                 //演算結果格納
    boolean operationKeyPushed;  //演算記号が入力されているか

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初期化
        initApp();
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
        //演算記号を取得
        findViewById(R.id.button_Addition).setOnClickListener(onClickOperation);
        findViewById(R.id.button_Subtraction).setOnClickListener(onClickOperation);
        findViewById(R.id.button_Multiplication).setOnClickListener(onClickOperation);
        findViewById(R.id.button_Division).setOnClickListener(onClickOperation);
        findViewById(R.id.button_Equal).setOnClickListener(onClickOperation);
        //その他のボタンを取得
        findViewById(R.id.button_C).setOnClickListener(onClickClear);
        findViewById(R.id.button_CA).setOnClickListener(onClickClearAll);
        findViewById(R.id.button_DEL).setOnClickListener(onClickDelete);
        findViewById(R.id.button_PoM).setOnClickListener(onClickPoM);
        findViewById(R.id.button_Percentage).setOnClickListener(onClickPercentage);
        findViewById(R.id.button_SqRt).setOnClickListener(onClickSqRt);
    }

    //初期化
    void initApp() {
        oldFormula = (TextView) findViewById(R.id.old_formula);
        currentFormula = (TextView) findViewById(R.id.current_formula);
        recentOperation = R.id.button_Equal;
        operationKeyPushed = true;
        result = 0;
        currentFormula.setText("0");
        oldFormula.setText("");
    }

    //入力テキスト更新
    void updateCurrentNumber(String currentText){
        double checkVar = Double.parseDouble(currentText);
        if(checkVar == (long)checkVar)
            currentFormula.setText(String.valueOf((long)checkVar));
        else
            currentFormula.setText(String.valueOf(checkVar));
    }
    void updateCurrentNumber(double currentText){
        if(currentText == (long)currentText)
            currentFormula.setText(String.valueOf((long)currentText));
        else
            currentFormula.setText(String.valueOf(currentText));
    }

    //履歴テキスト更新
    void updateOldNumber(String oldText){
        double checkVar = Double.parseDouble(oldText);
        if(checkVar == (long)checkVar)
            oldFormula.setText(String.valueOf((long)checkVar));
        else
            oldFormula.setText(String.valueOf(checkVar));
    }

    //「C」が入力された時の処理
    View.OnClickListener onClickClear = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //演算記号が入力されている状態に
            operationKeyPushed = true;
            //入力テキストのみ消去
            currentFormula.setText("0");
        }
    };

    //「CA」が入力された時の処理
    View.OnClickListener onClickClearAll = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //情報を初期化
            recentOperation = R.id.button_Equal;
            //演算記号が入力されている状態に
            operationKeyPushed = true;
            //演算結果を0に
            result = 0;
            //入力テキストを消去
            currentFormula.setText("0");
            //履歴テキストを消去
            oldFormula.setText("");
        }
    };

    //「DEL」が入力された時の処理
    View.OnClickListener onClickDelete = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //入力テキストを取得
            String currentText = currentFormula.getText().toString();
            //currentTextの末尾を消去
            if(currentText.length() >= 2){
                currentText = currentText.substring(0, currentText.length() - 1);
                //入力テキストを更新
                updateCurrentNumber(currentText);
            }
            else
                currentFormula.setText("0");
        }
    };

    //「+/-」が入力された時の処理
    View.OnClickListener onClickPoM = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //入力テキストの数値を取得
            double conversion = Double.parseDouble(currentFormula.getText().toString());
            //+/-変換
            conversion -= conversion * 2;
            //入力テキストに表示
            updateCurrentNumber(conversion);
            //演算記号が入力されている状態に
            operationKeyPushed = true;
        }
    };

    //「%」が入力された時の処理
    View.OnClickListener onClickPercentage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //入力テキストの数値を取得
            double value = Double.parseDouble(currentFormula.getText().toString());
            //直前の記号が「+」か「-」の場合演算実行
            if(recentOperation == R.id.button_Addition || recentOperation == R.id.button_Subtraction)
                result = calculation(recentOperation, result, (result * (value / 100)));
            //演算結果を入力テキストに表示
            updateCurrentNumber(result);
            //履歴テキストを消去
            oldFormula.setText("");
            //演算記号が入力されている状態に
            operationKeyPushed = true;
        }
    };

    //「√」が入力された時の処理
    View.OnClickListener onClickSqRt = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //入力テキストの数値を取得
            double conversion = Double.parseDouble(currentFormula.getText().toString());
            //平方根を計算
            conversion = sqrt(conversion);
            //入力テキストに表示
            updateCurrentNumber(conversion);
            //演算記号が入力されている状態に
            operationKeyPushed = true;
        }
    };

    //数字が入力された時の処理
    View.OnClickListener onClickNumber = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //数字を取得
            Button button = (Button) view;
            //直前に記号が入力されていた場合
            if (operationKeyPushed) {
                //ドットが入力された場合
                if (button.getId() == R.id.button_Dot) {
                    currentFormula.append(button.getText());
                    //演算記号が入力されていない状態に
                    operationKeyPushed = false;
                }
                //数字が入力された場合、新しく数字を表示
                else{
                    //「0」か「00」が入力された場合
                    if(button.getId() == R.id.button_0 || button.getId() == R.id.button_00)
                        currentFormula.setText("0");
                    //それ以外の数字が入力された場合
                    else {
                        currentFormula.setText(button.getText());
                        //演算記号が入力されていない状態に
                        operationKeyPushed = false;
                    }
                }
            }
            //直前に数字が入力されていた場合
            else {
                //既存の数字の後に入力された数字を追加
                currentFormula.append(button.getText());
                //演算記号が入力されていない状態に
                operationKeyPushed = false;
            }
        }
    };

    //記号が入力された時の処理
    View.OnClickListener onClickOperation = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //演算記号を取得
            Button operationButton = (Button) view;
            //演算記号が入力される前の数値を取得
            double value = Double.parseDouble(currentFormula.getText().toString());
            //一個前に入力された演算記号が「=」の場合
            if (recentOperation == R.id.button_Equal) {
                result = value;
                //履歴テキストに「入力フィールドの値 演算記号」を表示
                if(result == (long)result)
                    oldFormula.setText(String.valueOf((long)result) + " " + operationButton.getText());
                else
                    oldFormula.setText(String.valueOf(result) + " " + operationButton.getText());
            }
            //一個前に入力された演算記号が「=」以外の場合
            else {
                //演算記号が連続で入力された場合
                if(operationKeyPushed && operationButton.getId() != R.id.button_Equal){
                    //入力された演算記号が「=」以外の場合
                    if(operationButton.getId() != R.id.button_Equal) {
                        //履歴テキストに「入力フィールドの値 演算記号」を表示
                        if (result == (long) result)
                            oldFormula.setText(String.valueOf((long) result) + " " + operationButton.getText());
                        else
                            oldFormula.setText(String.valueOf(result) + " " + operationButton.getText());
                    }
                    //入力された演算記号が「=」の場合
                    else{
                        //履歴テキストを消去
                        oldFormula.setText("");
                        //計算実行
                        result = calculation(recentOperation, result, value);
                        //計算結果を入力テキストに表示
                        updateCurrentNumber(result);
                    }
                }
                //演算記号が連続で入力されてない場合
                else {
                    //計算実行
                    result = calculation(recentOperation, result, value);
                    //計算結果を入力テキストに表示
                    updateCurrentNumber(result);
                    //履歴テキストを更新
                    if(value == (long)value) oldFormula.append(String.valueOf((long)value) + " " + operationButton.getText());
                    else oldFormula.append(String.valueOf(value) + " " + operationButton.getText());
                    //今回入力されたのが「=」の場合、履歴テキストを消去
                    if (operationButton.getId() == R.id.button_Equal) oldFormula.setText("");
                }
            }
            //入力された演算記号を更新
            recentOperation = operationButton.getId();
            //演算記号が入力されている状態に
            operationKeyPushed = true;
        }
    };

    //演算メソッド
    double calculation(int operation, double value1, double value2) {
        //演算記号によって分岐
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
            default:
                return value1;
        }
    }
}

