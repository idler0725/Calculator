package com.example.idler0725.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static java.lang.Math.sqrt;

public class MainActivity extends AppCompatActivity {
    //変数宣言
    TextView old_formula;             //履歴フィールド
    TextView current_formula;        //入力フィールド
    int recentOperation;            //演算記号格納
    double result;                   //演算結果格納
    boolean operationKeyPushed;    //演算記号が押されているか

    //初期化
    void initApp() {
        recentOperation = R.id.button_Equal;
        result = 0;
        operationKeyPushed = true;
        old_formula = (TextView) findViewById(R.id.old_formula);
        current_formula = (TextView) findViewById(R.id.current_formula);
        current_formula.setText("0");
        findViewById(R.id.button_0).setEnabled(false);
        findViewById(R.id.button_00).setEnabled(false);
    }

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
        findViewById(R.id.button_SqRt).setOnClickListener(onClickSqRt);
        findViewById(R.id.button_PoM).setOnClickListener(onClickPoM);
    }

    //「C」が押された時の処理
    View.OnClickListener onClickClear = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //入力フィールドのみ消去
            operationKeyPushed = true;
            current_formula.setText("0");
            findViewById(R.id.button_0).setEnabled(false);
            findViewById(R.id.button_00).setEnabled(false);
        }
    };

    //「CA」が押された時の処理
    View.OnClickListener onClickClearAll = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //入力状態を消去
            recentOperation = R.id.button_Equal;
            result = 0;
            operationKeyPushed = true;
            old_formula.setText("");
            current_formula.setText("0");
            findViewById(R.id.button_0).setEnabled(false);
            findViewById(R.id.button_00).setEnabled(false);
        }
    };

    //「DEL」が押された時の処理
    View.OnClickListener onClickDelete = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    //「+/-」が押された時の処理
    View.OnClickListener onClickPoM = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //入力フィールドの数値を取得
            double conversion = Double.parseDouble(current_formula.getText().toString());
            //+/-変換
            conversion -= conversion * 2;
            //入力フィールドに表示
            if(conversion == (long)conversion) current_formula.setText(String.valueOf((long)conversion));
            else current_formula.setText(String.valueOf(conversion));
            operationKeyPushed = true;
        }
    };

    //「√」が押された時の処理
    View.OnClickListener onClickSqRt = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //入力フィールドの数値を取得
            double conversion = Double.parseDouble(current_formula.getText().toString());
            //平方根を計算
            conversion = sqrt(conversion);
            //入力フィールドに表示
            if(conversion == (long)conversion) current_formula.setText(String.valueOf((long)conversion));
            else current_formula.setText(String.valueOf(conversion));
            operationKeyPushed = true;
        }
    };

    //数字が押された時の処理
    View.OnClickListener onClickNumber = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;      //数字を取得
            //直前に記号が入力されていた場合
            if (operationKeyPushed == true) {
                //ドットが押された場合
                if(button.getId() == R.id.button_Dot){
                    //0の後にドットを表示
                    current_formula.setText("0");
                    current_formula.append(button.getText());
                }
                //数字が押された場合、新しく数字を表示
                else current_formula.setText(button.getText());
            }
            //直前に数字が入力されていた場合
            else {
                //ドットが押され、0しか入力されていなかった場合、先に0を入力
                if(button.getId() == R.id.button_Dot && current_formula.getText().toString() == "0") current_formula.setText("0");
                //既存の数字の後に押された数字を追加
                current_formula.append(button.getText());
            }
            //記号が押されていない状態に
            operationKeyPushed = false;
            //0しか入力されてなければ「0」「00」は入力できない
            double check = Double.parseDouble(current_formula.getText().toString());
            if(check == 0.0){
                findViewById(R.id.button_0).setEnabled(false);
                findViewById(R.id.button_00).setEnabled(false);
            }
            else
            {
                findViewById(R.id.button_0).setEnabled(true);
                findViewById(R.id.button_00).setEnabled(true);
            }
        }
    };

    //記号が押された時の処理
    View.OnClickListener onClickOperation = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button operationButton = (Button) view;                                         //記号を取得
            double value = Double.parseDouble(current_formula.getText().toString());    //記号が押される前の数値を取得
            //一個前に押された記号が「=」の場合
            if (recentOperation == R.id.button_Equal) {
                result = value;
                //今回押されたのが「=」の場合、履歴フィールドを消去
                if (operationButton.getId() == R.id.button_Equal) old_formula.setText("");
                //今回押されたのが「=」以外の場合
                else {
                    //履歴フィールドに「入力フィールドの値 演算記号」を表示
                    if(result == (long)result) old_formula.setText(String.valueOf((long)result) + " " + operationButton.getText());
                    else old_formula.setText(String.valueOf(result) + " " + operationButton.getText());
                }
            }
            //一個前に押された記号が「=」以外の場合
            else {
                //記号が連続で押され、押された記号が「=」以外の場合
                if(operationKeyPushed == true && operationButton.getId() != R.id.button_Equal){
                    //履歴フィールドを更新
                    if(result == (long)result) old_formula.setText(String.valueOf((long)result) + " " + operationButton.getText());
                    else old_formula.setText(String.valueOf(result) + " " + operationButton.getText());
                }
                //記号が連続で押されてない場合
                else {
                    //計算実行
                    result = calculation(recentOperation, result, value);
                    //計算結果を入力フィールドに表示
                    if(result == (long)result) current_formula.setText(String.valueOf((long)result));
                    else current_formula.setText(String.valueOf(result));
                    //履歴フィールドを更新
                    if(value == (long)value) old_formula.append(String.valueOf(value) + " " + operationButton.getText());
                    else old_formula.append(String.valueOf((long)value) + " " + operationButton.getText());
                    //今回押されたのが「=」の場合、履歴フィールドを消去
                    if (operationButton.getId() == R.id.button_Equal) old_formula.setText("");
                }
            }
            //押された記号を更新
            recentOperation = operationButton.getId();
            //記号が押されている状態に
            operationKeyPushed = true;
            //0しか入力されてなければ「0」「00」は入力できない
            double check = Double.parseDouble(current_formula.getText().toString());
            if(check == 0.0){
                findViewById(R.id.button_0).setEnabled(false);
                findViewById(R.id.button_00).setEnabled(false);
            }
            else
            {
                findViewById(R.id.button_0).setEnabled(true);
                findViewById(R.id.button_00).setEnabled(true);
            }
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
            default:
                return value1;
        }
    }
}

