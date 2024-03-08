package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// MASUKKAN IMPORT BERIKUT INI :
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    // ID semua tombol numerik
    private int[] tombolNumeric = {R.id.nol, R.id.satu, R.id.dua, R.id.tiga, R.id.empat, R.id.lima, R.id.enam, R.id.tujuh, R.id.delapan, R.id.sembilan};

    // ID semua tombol operator
    private int[] tombolOperator = {R.id.kali, R.id.bagi, R.id.tambah, R.id.kurang};

    // untuk menampilkan output penjumlahan
    private TextView hasil;

    // untuk menampilkan output hasil
    private TextView hasil2;

    // mengecek apakah tombol yang terakhir ditekan bersifat numerik atau tidak
    private boolean angkaTerakhir;

    // mengecek bahwa keadaan saat ini sedang error atau tidak
    private boolean kaloError;

    // jika benar, jangan izinkan menambahkan koma lain
    private boolean setelahTitik;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.hasil = (TextView) findViewById(R.id.hasil);
        this.hasil2 = (TextView) findViewById(R.id.hasil2);

        setNumericPadaSaatDiKlik();
        setOperatorPadaSaatDiKlik();
    }

    private void setNumericPadaSaatDiKlik() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button tombol = (Button) v;
                if (kaloError) {
                    hasil.setText(tombol.getText());
                    kaloError = false;

                } else {
                    hasil.append(tombol.getText());
                }

                angkaTerakhir = true;
            }
        };

        for (int id : tombolNumeric) {
            findViewById(id).setOnClickListener(listener);
        }

    }

    private void setOperatorPadaSaatDiKlik() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (angkaTerakhir && !kaloError) {
                    Button tombol = (Button) v;
                    hasil.append(tombol.getText());
                    angkaTerakhir = false;
                    setelahTitik = false;
                }
            }
        };

        for (int id : tombolOperator) {
            findViewById(id).setOnClickListener(listener);
        }
        findViewById(R.id.koma).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (angkaTerakhir && !kaloError && !setelahTitik) {
                    hasil.append(".");
                    angkaTerakhir = false;
                    setelahTitik = true;
                }
            }

        });
        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            //fungsi tombol clear
            public void onClick(View v) {
                //mereset semua input dan output
                hasil.setText("");
                hasil2.setText("");

                angkaTerakhir = false;
                setelahTitik = false;
                kaloError = false;
            }
        });

        findViewById(R.id.sama_dengan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onEqual();

            }

            private void onEqual() {
                if (angkaTerakhir && !kaloError) {
                    String txt = hasil.getText().toString();
                    Expression expression = new ExpressionBuilder(txt).build();
                    try {
                        // menghitung hasil dan menampilkan
                        double result = expression.evaluate();
                        hasil2.setText(Double.toString(result));
                        if (result == (int) result) {
                            // jika angka bulat, hapus koma
                            hasil2.setText(Integer.toString((int) result));
                        } else {
                            // jika angka tidak bulat, tampilkan koma
                            hasil2.setText(Double.toString(result));
                        }
                        setelahTitik = true;
                    } catch (ArithmeticException ex) {
                        // menampilkan text salah (jika ada)
                        hasil2.setText(("ERROR WOY"));
                        kaloError = true;
                        angkaTerakhir = false;
                    }
                }
            }
        });
    }

    // funsi tombol backspace
    public void hapus(View view) {
        String kata = hasil.getText().toString();
        int input = kata.length();
        if (input > 0){
            hasil.setText(kata.substring(0, input -1));
        }
    }
}