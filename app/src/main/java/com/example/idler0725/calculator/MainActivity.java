package com.example.idler0725.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import static java.lang.Math.sqrt;

public class MainActivity extends AppCompatActivity {
    //変数
    TextView oldFormula;           //履歴テキスト
    TextView currentFormula;      //入力テキスト
    TextView oldText;               //履歴編集用
    TextView currentText;          //入力編集用
    String memory;                  //メモリー機能用
    int recentOperation;          //演算記号格納
    int numberLength;               //桁
    double result;                 //演算結果格納
    double currentDouble;         //double型に直したもの
    boolean operationKeyPushed;  //演算記号が入力されているか
    boolean inputDot;               //ドット入力用
    //定数
    final int inputMax = 12;
    final double numberMax = 999999999999d;
    final double numberMin = -999999999999d;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        //初期化
        initApp();
        //数字を取得
        findViewById(R.id.button_1).setOnClickListener(onClickButton);
        findViewById(R.id.button_2).setOnClickListener(onClickButton);
        findViewById(R.id.button_3).setOnClickListener(onClickButton);
        findViewById(R.id.button_4).setOnClickListener(onClickButton);
        findViewById(R.id.button_5).setOnClickListener(onClickButton);
        findViewById(R.id.button_6).setOnClickListener(onClickButton);
        findViewById(R.id.button_7).setOnClickListener(onClickButton);
        findViewById(R.id.button_8).setOnClickListener(onClickButton);
        findViewById(R.id.button_9).setOnClickListener(onClickButton);
        findViewById(R.id.button_0).setOnClickListener(onClickButton);
        findViewById(R.id.button_00).setOnClickListener(onClickButton);
        findViewById(R.id.button_Dot).setOnClickListener(onClickButton);
        //演算記号を取得
        findViewById(R.id.button_Addition).setOnClickListener(onClickButton);
        findViewById(R.id.button_Subtraction).setOnClickListener(onClickButton);
        findViewById(R.id.button_Multiplication).setOnClickListener(onClickButton);
        findViewById(R.id.button_Division).setOnClickListener(onClickButton);
        findViewById(R.id.button_Equal).setOnClickListener(onClickButton);
        //その他のボタンを取得
        findViewById(R.id.button_C).setOnClickListener(onClickButton);
        findViewById(R.id.button_CA).setOnClickListener(onClickButton);
        findViewById(R.id.button_DEL).setOnClickListener(onClickButton);
        findViewById(R.id.button_PoM).setOnClickListener(onClickButton);
        findViewById(R.id.button_Percentage).setOnClickListener(onClickButton);
        findViewById(R.id.button_SqRt).setOnClickListener(onClickButton);
        //メモリー機能
        findViewById(R.id.button_CLEAR_MEMORY).setOnClickListener(onClickButton);
        findViewById(R.id.button_SAVE_MEMORY).setOnClickListener(onClickButton);
        findViewById(R.id.button_LOAD_MEMORY).setOnClickListener(onClickButton);
    }

    //ここから場合分け
    View.OnClickListener onClickButton = new View.OnClickListener () {
        @Override
        public void onClick (View view) {
            //入力を取得
            Button button = (Button) view;
            //現在の表示を取得
            oldText = oldFormula;
            String temp = currentFormula.getText().toString();
            currentText.setText(temp);
            //double型にしたものをよく使うので作っておく
            try {
                currentDouble = Double.parseDouble(currentText.getText().toString().replaceAll(",", ""));
            }
            catch (NumberFormatException e) {

            }
            //currentDouble = Double.parseDouble(currentText.getText().toString().replaceAll(",", ""));
            //入力情報をチェック
            currentCheck (currentDouble);
            //桁数を取得
            temp = currentText.getText().toString().replaceAll(",", "");
            numberLength = temp.replaceAll("\\.", "").length();

            //「1~9」の数字が入力された場合
            if (button.getId() == R.id.button_1 || button.getId() == R.id.button_2 || button.getId() == R.id.button_3 ||
                    button.getId() == R.id.button_4 || button.getId() == R.id.button_5 || button.getId() == R.id.button_6 ||
                    button.getId() == R.id.button_7 || button.getId() == R.id.button_8 || button.getId() == R.id.button_9)
                onClickNumber(button);
            //「0,00」が入力された場合
            else if (button.getId() == R.id.button_0 || button.getId() == R.id.button_00)
                onClickZero(button);
            //「.」が入力された場合
            else if (button.getId() == R.id.button_Dot)
                onClickDot(button);

            //「DEL」が入力された場合
            else if (button.getId() == R.id.button_DEL)
                onClickDelete();

            //その他記号が入力された場合
            else
            {
                switch (button.getId()){
                    case R.id.button_Addition :
                    case R.id.button_Subtraction :
                    case R.id.button_Multiplication :
                    case R.id.button_Division :
                    case R.id.button_Equal : onClickOperation(button); break;

                    case R.id.button_C :            onClickClear();         break;
                    case R.id.button_CA :           onClickClearAll();     break;
                    case R.id.button_PoM :          onClickPoM();           break;
                    case R.id.button_Percentage :  onClickPercentage();   break;
                    case R.id.button_SqRt :         onClickSqRt();          break;

                    case R.id.button_CLEAR_MEMORY : onClickCM();          break;
                    case R.id.button_SAVE_MEMORY :  onClickSM();          break;
                    case R.id.button_LOAD_MEMORY :  onClickLM();          break;
                }
                //記号が入力されている状態に
                operationKeyPushed = true;
                //ドットの入力情報を更新
                inputDot = false;
            }
            //表示処理
            displayText(button);
        }
    };

    //初期化
    void initApp () {
        //テキストの取得
        oldFormula = (TextView) findViewById(R.id.old_formula);
        currentFormula = (TextView) findViewById(R.id.current_formula);
        oldText = new TextView(this);
        currentText = new TextView(this);
        //テキストを初期化
        currentFormula.setText("0");
        oldFormula.setText("");
        //記号情報を初期化
        recentOperation = R.id.button_Equal;
        //値を初期化
        numberLength = 1;
        result = 0;
        currentDouble = 0;
        //フラグを初期化
        operationKeyPushed = true;
        inputDot = false;
    }

    //入力テキストチェック
    void currentCheck (double currentNumber) {
        //入力テキストが無限、数値以外、範囲外の場合
        if (Double.isNaN(currentNumber) || Double.isInfinite(currentNumber) ||
                currentNumber > numberMax || currentNumber < numberMin) {
            //テキストを初期状態に
            oldText.setText("");
            currentText.setText("0");
            //記号情報をリセット
            recentOperation = R.id.button_Equal;
            //値をリセット
            numberLength = 1;
            result = 0;
            currentDouble = 0;
            //フラグをリセット
            operationKeyPushed = true;
            inputDot = false;
        }
    }

    //表示処理
    void displayText(Button input){
        //再定義
        try {
            currentDouble = Double.parseDouble(currentText.getText().toString().replaceAll(",", ""));
        }
        catch (NumberFormatException e) {

        }
        //数値の場合
        if(!Double.isNaN(currentDouble) && !Double.isInfinite(currentDouble)){
            //テキストを整理
            BigDecimal value = BigDecimal.valueOf(currentDouble);
            DecimalFormat comma = new DecimalFormat("#,##0.###########");
            DecimalFormat index = new DecimalFormat("0.000E0");
            String temp;
            //整数の場合
            if(!inputDot){
                //13桁以上の場合は見えないギリギリのところを四捨五入
                value = value.setScale((inputMax - 1), RoundingMode.HALF_UP);
                //整数部3ケタ毎にカンマを表示
                temp = comma.format(value);
                //13桁以上は指数に変換
                if (currentDouble > numberMax || currentDouble < numberMin)
                    temp = index.format(value);
                //テキストを更新
                currentText.setText(temp);
            }
        }
        //表示を更新
        oldFormula.setText(oldText.getText());
        currentFormula.setText(currentText.getText());
    }

    //「1~9」が入力された場合
    void onClickNumber (Button numberButton) {
        //入力テキストの数値を取得
        BigDecimal temp = BigDecimal.valueOf(currentDouble);
        //12桁未満の場合 or 記号が入力されていた場合
        if(numberLength < inputMax || operationKeyPushed){
            //「0」しか入力されていない場合 or 記号が入力されていた場合は入力された数字をセット
            if(temp.compareTo(BigDecimal.valueOf(0)) == 0 && !inputDot || operationKeyPushed)
                currentText.setText(numberButton.getText());
            //すでに数字が入力されている場合はそのまま後ろに追加
            else
                currentText.append(numberButton.getText());
            //記号が入力されていない状態に
            operationKeyPushed = false;
        }
    }

    //「0,00」が入力された場合
    void onClickZero (Button numberButton) {
        //入力テキストの数値を取得
        BigDecimal temp = BigDecimal.valueOf(currentDouble);
        //12桁未満の場合 or 記号が入力されていた場合
        if(numberLength < inputMax || operationKeyPushed){
            //「0」しか入力されていない場合 or 記号が入力されていた場合は「0」をセット
            if(temp.compareTo(BigDecimal.valueOf(0)) == 0 && !inputDot || operationKeyPushed)
                currentText.setText("0");
            //すでに数字が入力されている場合
            else{
                //11桁入力されている場合 & 「00」が「入力された場合は「0」をセット
                if(numberLength == inputMax - 1 && numberButton.getId() == R.id.button_00)
                    currentText.append("0");
                //それ以外の場合はそのまま後ろに追加
                else
                    currentText.append(numberButton.getText());
            }
            //記号が入力されていない状態に
            operationKeyPushed = false;
        }
    }

    //「.」が入力された場合
    void onClickDot (Button numberButton) {
        //入力テキストの数値を取得
        BigDecimal temp = BigDecimal.valueOf(currentDouble);
        //「.」が入力されてない場合
        if(!inputDot){
            //0しか入力されていない場合 or 記号が入力されていた場合
            if (temp.compareTo(BigDecimal.valueOf(0)) == 0 || operationKeyPushed) {
                //0をセット
                currentText.setText("0");
                //後ろに「.」を追加
                currentText.append(numberButton.getText());
            }
            //数字が小数でない場合は後ろに「.」を追加
            else if(currentDouble == (long)currentDouble)
                currentText.append(numberButton.getText());
            //記号が入力されていない状態に
            operationKeyPushed = false;
            //「.」の入力情報を更新
            inputDot = true;
        }
    }

    //「DEL」が入力された場合
    void onClickDelete () {
        //－の値の場合 or 最後の1文字の場合は0をセット
        if (numberLength == 2 && currentDouble < 0 || numberLength <= 1)
            currentText.setText("0");
        //2文字以上ある場合の削除
        else {
            currentText.setText(currentText.getText().toString().substring(0, currentText.length() - 1));
            if(currentText.getText().toString().charAt(currentText.length() - 1) == '.'){
                currentText.setText(currentText.getText().toString().substring(0, currentText.length() - 1));
                inputDot = false;
            }
        }
        //記号が入力されていない状態に
        operationKeyPushed = false;
    }

    //演算記号が入力された場合
    void onClickOperation (Button operationButton) {
        //演算記号が入力される前の数値を取得
        double value = currentDouble;
        //一個前に入力された記号が「=」の場合
        if (recentOperation == R.id.button_Equal) {
            result = value;
            //今回入力された記号が「=」の場合、履歴テキストを消去
            if (operationButton.getId() == R.id.button_Equal)
                oldText.setText("");
            //「=」以外が入力された場合、履歴テキストを更新
            else {
                if(result == (long)result)
                    oldText.setText(String.valueOf((long) result));
                else
                    oldText.setText(String.valueOf(result));
                oldText.append(operationButton.getText());
            }
        }
        //一個前に入力された記号が「=」以外の場合
        else {
            //今回入力された記号が「=」の場合
            if (operationButton.getId() == R.id.button_Equal) {
                //計算実行
                result = calculation(recentOperation, result, value);
                //履歴テキストを消去
                oldText.setText("");
                //入力テキストを更新
                currentText.setText(String.valueOf(result));
            }
            //演算記号が連続で入力された場合、履歴テキストを更新
            else if (operationKeyPushed) {
                if(result == (long)result)
                    oldText.setText(String.valueOf((long) result));
                else
                    oldText.setText(String.valueOf(result));
                oldText.append(operationButton.getText());
            }
            //演算記号が連続で入力されてない場合
            else {
                //計算実行
                result = calculation(recentOperation, result, value);
                //履歴テキストを更新
                if(result == (long)result)
                    oldText.append(String.valueOf((long) value));
                else
                    oldText.append(String.valueOf(value));
                oldText.append(operationButton.getText());
                //入力テキストを更新
                currentText.setText(String.valueOf(result));
            }
        }
        //入力された記号を更新
        recentOperation = operationButton.getId();
    }

    //「C」が入力された場合
    void onClickClear () {
        //入力テキストを消去
        currentText.setText("0");
    }

    //「CA」が入力された場合
   void onClickClearAll () {
        //テキストをリセット
        currentText.setText("0");
        oldText.setText("");
       //記号情報を初期化
       recentOperation = R.id.button_Equal;
       //値を初期化
       result = 0;
    }

    //「CM」が入力された場合
    void onClickCM () {
        memory = "";
    }

    //「SM」が入力された場合
    void onClickSM () {
        memory = currentText.getText().toString();
    }

    //「LM」が入力された場合
    void onClickLM () {
        currentText.setText(memory);
    }

    //「+/-」が入力された場合
    void onClickPoM () {
        //入力テキストの数値を取得 & 変換
        BigDecimal value = BigDecimal.valueOf(currentDouble).negate();
        //入力テキストを更新
        currentText.setText(String.valueOf(value));
    }

    //「%」が入力された場合
    void onClickPercentage () {
        //入力テキストの数値を取得
        double value = currentDouble;
        //直前の記号が「+」か「-」の場合演算実行
        if (recentOperation == R.id.button_Addition || recentOperation == R.id.button_Subtraction)
            result = calculation(recentOperation, result, result * (value / 100));
        //履歴テキストを消去
        oldFormula.setText("");
        //入力テキストを更新
        currentText.setText(String.valueOf(result));
        //記号情報をリセット
        recentOperation = R.id.button_Equal;
    }

    //「√」が入力された場合
    void onClickSqRt () {
        //入力テキストの数値を取得
        double value = currentDouble;
        //平方根を計算
        value = sqrt(value);
        //入力テキストを更新
        currentText.setText(String.valueOf(value));
    }

    //演算メソッド
    double calculation(int operation, double old, double current) {
        BigDecimal value1 = BigDecimal.valueOf(old);
        BigDecimal value2 = BigDecimal.valueOf(current);
        BigDecimal calcResult;
        //演算記号によって分岐
        switch (operation) {
            //足し算
            case R.id.button_Addition:
                calcResult = value1.add(value2);
                break;
            //引き算
            case R.id.button_Subtraction:
                calcResult = value1.subtract(value2);
                break;
            //掛け算
            case R.id.button_Multiplication:
                calcResult = value1.multiply(value2);
                break;
            //割り算
            case R.id.button_Division:
                try {
                    calcResult = value1.divide(value2, inputMax - 1, RoundingMode.HALF_UP);
                }
                catch (ArithmeticException e) {
                    return Double.parseDouble(value1.toString()) / Double.parseDouble(value2.toString());
                }
                break;
            default:
                calcResult = value1;
        }
        //演算結果を返す
        return Double.parseDouble(calcResult.toString());
    }
}

