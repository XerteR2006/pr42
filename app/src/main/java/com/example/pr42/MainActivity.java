package com.example.pr42;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView textView;
    private Button deleteButton;
    private Button addButton;
    private EditText editTextName;
    private EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        deleteButton = findViewById(R.id.button);
        addButton = findViewById(R.id.button2);
        editTextName = findViewById(R.id.editTextText);
        editTextEmail = findViewById(R.id.editTextText2);

        // Убедитесь, что у вас есть корневой элемент с id main в вашем XML-файле
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = new DatabaseHelper(this);

        // Добавление тестовых пользователей (для примера)
        databaseHelper.addUser(new User(0, "Роман ", "megadrad@kul."));
        databaseHelper.addUser(new User(0, "Не роман", "not@Roman.com"));

        // Отображение пользователей
        displayUsers();

        // Установка обработчика нажатия на кнопку удаления
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLastUser();
            }
        });

        // Установка обработчика нажатия на кнопку добавления
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
    }

    private void displayUsers() {
        List<User> users = databaseHelper.getAllUsers();
        StringBuilder stringBuilder = new StringBuilder();

        for (User user : users) {
            stringBuilder.append(user.toString()).append("\n");
        }

        textView.setText(stringBuilder.toString());
    }

    private void deleteLastUser() {
        List<User> users = databaseHelper.getAllUsers();
        if (!users.isEmpty()) {
            // Удаляем последнего пользователя
            User lastUser = users.get(users.size() - 1);
            databaseHelper.deleteUser(lastUser.getId()); // Предполагается, что у вас есть метод deleteUser в DatabaseHelper
            displayUsers(); // Обновляем отображение пользователей
        }
    }

    private void addUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Добавляем нового пользователя в базу данных
        databaseHelper.addUser(new User(0, name, email));

        // Очищаем поля ввода
        editTextName.setText("");
        editTextEmail.setText("");

        // Обновляем отображение пользователей
        displayUsers();
    }
}

