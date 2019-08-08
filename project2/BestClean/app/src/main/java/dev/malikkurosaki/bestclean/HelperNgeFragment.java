package dev.malikkurosaki.bestclean;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

class HelperNgeFragment {

    private FragmentManager manager;
    private FragmentTransaction transaction;
    private int id;

    HelperNgeFragment(FragmentManager managerNya,FragmentTransaction transactionNya,int idnYa){
        this.manager = managerNya;
        this.transaction = transactionNya;
        this.id = idnYa;
    }

    public void ngeFragment(Fragment fragment){
        String tag = fragment.getClass().getName();

        if (manager.findFragmentByTag(tag) == null){
            transaction.add(id,fragment,tag);
            transaction.addToBackStack(tag);
            transaction.commitAllowingStateLoss();
        }else {
            transaction.show(manager.findFragmentByTag(tag)).commitAllowingStateLoss();
        }
    }
}
