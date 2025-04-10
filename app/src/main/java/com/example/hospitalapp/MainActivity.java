package com.example.hospitalapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
//androidx.appcompat.app.AppCompatActivity — это базовый (родительский) класс для всех экранов.
//Он добавляет поддержку старых и новых версий Android.
import androidx.viewpager.widget.ViewPager;
//ViewPager позволяет пользователю переключаться между экранами свайпом влево и вправо.
import com.google.android.material.tabs.TabLayout;
//TabLayout — это панель с вкладками вверху, как в браузере. Помогает переключаться между фрагментами.

public class MainActivity extends AppCompatActivity {
    // public — означает, что этот класс доступен из других частей программы.
    //class — ключевое слово, создаёт новый класс (набор функций и данных).
    //MainActivity — имя класса. Это главный экран приложения.
    //extends AppCompatActivity — означает "наследуемся" от AppCompatActivity.
    //Мы берём все возможности AppCompatActivity, плюс добавим свои.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //@Override — говорит, что мы переопределяем (меняем) поведение метода из родительского класса.
        //protected — модификатор доступа. Означает, что метод доступен в этом классе и у потомков.
        //void — метод ничего не возвращает.
        //onCreate — метод, который вызывается при создании экрана.
        //Bundle savedInstanceState — данные, сохранённые при прошлых запусках (если были).
        super.onCreate(savedInstanceState);
        //super — обращение к родительскому классу.
        //Мы вызываем родительскую версию onCreate, чтобы не сломать её поведение (важно!)
        setContentView(R.layout.activity_main);
        //setContentView(...) — говорит: "Покажи такой-то экран".
        //R.layout.activity_main — это ссылка на XML-файл res/layout/activity_main.xml,
        //где описан дизайн экрана: кнопки, вкладки и т.д.

        ViewPager viewPager = findViewById(R.id.viewPager);
        //ViewPager viewPager — создаём переменную типа ViewPager.
        //findViewById(...) — ищем элемент на экране по его ID.
        //R.id.viewPager — ID этого элемента в XML-файле.
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        //То же самое — только ищем панель вкладок (вверху экрана), чтобы привязать её к ViewPager

        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        //TabAdapter — это специальный класс, который управляет вкладками (фрагментами).
        //getSupportFragmentManager() — встроенный метод.
        //Возвращает менеджер, который управляет фрагментами внутри экрана.
        adapter.addFragment(new PatientsFragment(), "Пациенты");
        adapter.addFragment(new DoctorsFragment(), "Врачи");
        adapter.addFragment(new ArchiveFragment(), "Архив");
        //Мы добавляем три "вкладки" с названиями:
        //PatientsFragment — экран со списком пациентов
        //DoctorsFragment — экран с врачами
        //ArchiveFragment — архив данных
        //Каждая вкладка — это отдельный фрагмент (подэкран, часть UI)

        viewPager.setAdapter(adapter);
        //Говорим ViewPager, что теперь он будет использовать наш адаптер для отображения фрагментов.
        tabLayout.setupWithViewPager(viewPager);
        //Привязываем вкладки к ViewPager, чтобы при свайпе менялись и вкладки, и экраны.
}}