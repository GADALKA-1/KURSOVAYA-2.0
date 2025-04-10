package com.example.hospitalapp;

import androidx.fragment.app.Fragment;
//Fragment — это часть экрана (например, одна вкладка).
//Фрагменты позволяют разбить интерфейс на независимые блоки.
import androidx.fragment.app.FragmentManager;
//FragmentManager — класс, который управляет фрагментами: показывает, скрывает, переключает их.
import androidx.fragment.app.FragmentPagerAdapter;
//FragmentPagerAdapter — адаптер (связывающее звено) между ViewPager и фрагментами.
//Он говорит ViewPager, какой фрагмент показать на каком экране.

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentPagerAdapter {
    //public — класс доступен отовсюду.
    //class TabAdapter — имя класса.
    //extends FragmentPagerAdapter — означает: мы наследуемся от стандартного адаптера для фрагментов.
    //Наш класс будет своим адаптером, который знает, какие фрагменты показывать.
    private final List<Fragment> fragments = new ArrayList<>();
    private final List<String> titles = new ArrayList<>();

    public TabAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }
    //TabAdapter(...) — это конструктор — запускается при создании объекта.
    //FragmentManager fm — объект, управляющий фрагментами, передаётся из активности.
    //super(...) — вызывает конструктор родительского класса FragmentPagerAdapter.
    //BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT — говорит, что только текущий фрагмент будет активным, остальные — "поставлены на паузу" (экономия ресурсов).
    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        titles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
