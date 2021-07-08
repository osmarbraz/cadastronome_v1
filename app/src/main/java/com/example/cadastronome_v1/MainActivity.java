package com.example.cadastronome_v1;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //Dados do banco de dados
    final static String APP_PREFS = " cadastronome_v1 ";
    final static String USERNAME_KEY = " nome";

    //Componentes a serem manipulados
    private EditText EditTextNome;
    private TextView TextViewRegistros;
    private EditText EditTextListaDados;

    //Nome do banco de dados
    private SharedPreferences bdPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Associa os componentes da interface aos componentes da classe
        EditTextNome = findViewById(R.id.EditTextNome);
        TextViewRegistros = findViewById(R.id.TextViewRegistros);
        EditTextListaDados = findViewById(R.id.EditTextListaDados);

        //Associa o banco a classe
        bdPrefs = getSharedPreferences(MainActivity.APP_PREFS, MODE_PRIVATE);

        //Atualiza a quantidade de registros
        atualizaRegistros();
    }

    public void onClickIncluir(View v) {
        //Verifica se o EditTextNome foi preenchido
        if (!EditTextNome.getText().toString().equals("")) {
            //Recupera o nome
            String nome = EditTextNome.getText().toString();
            //Coloca o mapa em modo edição
            SharedPreferences.Editor editor = bdPrefs.edit();
            //Coloca o nome
            editor.putString(nome, nome);
            //Salva as alterações
            boolean resultado = editor.commit();
            if (resultado == true) {
                Toast.makeText(MainActivity.this, "Inclusão realizada com sucesso!", Toast.LENGTH_SHORT).show();
                atualizaRegistros();
            } else {
                Toast.makeText(MainActivity.this, "Inclusão não realizada!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Digite um nome!", Toast.LENGTH_SHORT).show();
            //Coloca o foco na caixa de texto nome
            EditTextNome.requestFocus();
        }
    }

    public void onClickConsultar(View v) {
        //Verifica se o nome foi preenchido
        if (!EditTextNome.getText().toString().equals("")) {
            //Recupera o nome
            String nome = EditTextNome.getText().toString();
            //Recupera o nome do banco de dados
            String nomeConsulta = bdPrefs.getString(nome, null);
            if (nomeConsulta != null) {
                Toast.makeText(MainActivity.this, "Nome encontrado!", Toast.LENGTH_SHORT).show();
                EditTextNome.requestFocus();
            } else {
                Toast.makeText(MainActivity.this, "Nome não encontrado!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Digite um nome!", Toast.LENGTH_SHORT).show();
            //Coloca o foco na caixa de texto nome
            EditTextNome.requestFocus();
        }
    }

    public void onClickExcluir(View v) {
        //Verifica se o EditTextNome foi preenchido
        if (!EditTextNome.getText().toString().equals("")) {
            //Recupera o nome
            String nome = EditTextNome.getText().toString();
            //Coloca o mapa em modo edição
            SharedPreferences.Editor editor = bdPrefs.edit();
            editor.remove(nome);
            editor.apply(); //Aplica as alterações
            Toast.makeText(MainActivity.this, "Nome removido!", Toast.LENGTH_SHORT).show();
            atualizaRegistros();
        } else {
            Toast.makeText(MainActivity.this, "Digite um nome!", Toast.LENGTH_SHORT).show();
            //Coloca o foco na caixa de texto EditTextNome
            EditTextNome.requestFocus();
        }
    }

    public void onClickEsvaziarBD(View v) {
        //Confirma a exclusão dos dados da tabela
        AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
        dialogo.setTitle("Esvaziar Dados"); //Título da janela de diálogo
        dialogo.setMessage("Deseja apagar todos os nomes?"); //Mensagem da janela de diálogo
        dialogo.setPositiveButton("Sim", new DialogInterface.OnClickListener() { //Evento para o botão sim
            public void onClick(DialogInterface dialog, int which) {
                //Ação para a resposta sim

                //Coloca o mapa em modo edição
                SharedPreferences.Editor editor = bdPrefs.edit();
                editor.clear();
                editor.commit();

                Toast.makeText(MainActivity.this, "Dados Apagados!", Toast.LENGTH_SHORT).show();
                atualizaRegistros();
            }
        });
        dialogo.setNegativeButton("Não", new DialogInterface.OnClickListener() { //Evento para o botão não
            public void onClick(DialogInterface dialog, int which) {
                //Ação para a resposta não

                //Apenas fecha a mensagem
                dialog.dismiss();
            }
        });
        dialogo.show(); //Exibe o diálogo
    }

    public void onClickListar(View v) {
        //Recupera a lista de todos os nomes aplicando o fitro sem atribuir nenhum valor ao objeto
        Map<String, Long> map = (Map<String, Long>) bdPrefs.getAll();
        //Cabeçalho da listagem
        String saida = "chave - nome\n";
        if (map != null) {
            //Percorre a lista recuperando os dados do objeto
            for (Map.Entry<String, Long> entry : map.entrySet()) {
                saida = saida + entry.getKey() + " - " + entry.getValue() + "\n";
                ;
            }
        }
        EditTextListaDados.setText(saida);
    }

    public void onClickLimpar(View v) {
        //Limpa os textos da inteface
        EditTextNome.setText("");
        EditTextListaDados.setText("");
        //Coloca o foco na caixa de texto nome
        EditTextNome.requestFocus();
    }

    public void onClickFechar(View v) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(MainActivity.this);
        dialogo.setTitle("Fechar aplicativo"); //Título da janela de diálogo
        dialogo.setMessage("Você tem certeza que deseja sair?"); //Mensagem da janela de diálogo
        dialogo.setPositiveButton("Sim", new DialogInterface.OnClickListener() { //Evento para o botão sim
            public void onClick(DialogInterface dialog, int which) {
                //Ação para a resposta sim
                Toast.makeText(MainActivity.this, "Fechando o aplicativo", Toast.LENGTH_SHORT).show();
                System.exit(0);
            }
        });
        dialogo.setNegativeButton("Não", new DialogInterface.OnClickListener() { //Evento para o botão não
            public void onClick(DialogInterface dialog, int which) {
                //Ação para a resposta não
                //Apenas fecha a mensagem
                dialog.dismiss();
            }
        });
        dialogo.show(); //Exibe o diálogo
    }

    public void atualizaRegistros() {
        //Recupera a lista de todos os nomes aplicando o fitro sem atribuir nenhum valor ao objeto
        Map<String, Long> map = (Map<String, Long>) bdPrefs.getAll();

        //Coloca a informação na interface
        TextViewRegistros.setText("Registros: " + map.size());
    }
}