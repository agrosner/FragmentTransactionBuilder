package com.grosner.fragmenttransactionbuilder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created By: andrewgrosner
 * Date: 8/30/13
 * Contributors:
 * Description: This class enables easy {@link android.support.v4.app.FragmentTransaction}
 * wrapping using the builder notation. This will support both native and support fragments eventually.
 */
public class FragmentTransactionBuilder {

    /**
     * The {@link android.support.v4.app.FragmentTransaction} mode that will be run.
     */
    public enum Mode {
        /**
         * @see {@link android.support.v4.app.FragmentTransaction#replace(int, android.support.v4.app.Fragment, String)}
         */
        REPLACE {
            @Override
            void perform(FragmentTransaction fragmentTransaction, int layout, Fragment fragment, String tag) {
                fragmentTransaction.replace(layout, fragment, tag);
            }
        },

        /**
         * @see {@link android.support.v4.app.FragmentTransaction#add(int, android.support.v4.app.Fragment, String)}
         */
        ADD {
            @Override
            void perform(FragmentTransaction fragmentTransaction, int layout, Fragment fragment, String tag) {
                fragmentTransaction.add(layout, fragment, tag);
            }
        },

        /**
         * @see {@link android.support.v4.app.FragmentTransaction#remove(android.support.v4.app.Fragment)}
         */
        REMOVE {
            @Override
            void perform(FragmentTransaction fragmentTransaction, int layout, Fragment fragment, String tag) {
                fragmentTransaction.remove(fragment);
            }
        },

        /**
         * @see {@link android.support.v4.app.FragmentTransaction#hide(android.support.v4.app.Fragment)}
         */
        HIDE {
            @Override
            void perform(FragmentTransaction fragmentTransaction, int layout, Fragment fragment, String tag) {
                fragmentTransaction.hide(fragment);
            }
        },

        /**
         * @see {@link android.support.v4.app.FragmentTransaction#show(android.support.v4.app.Fragment)}
         */
        SHOW {
            @Override
            void perform(FragmentTransaction fragmentTransaction, int layout, Fragment fragment, String tag) {
                fragmentTransaction.show(fragment);
            }
        },

        /**
         * @see {@link android.support.v4.app.FragmentTransaction#attach(android.support.v4.app.Fragment)}
         */
        ATTACH {
            @Override
            void perform(FragmentTransaction fragmentTransaction, int layout, Fragment fragment, String tag) {
                fragmentTransaction.attach(fragment);
            }
        },

        /**
         * @see {@link android.support.v4.app.FragmentTransaction#detach(android.support.v4.app.Fragment)}
         */
        DETACH {
            @Override
            void perform(FragmentTransaction fragmentTransaction, int layout, Fragment fragment, String tag) {
                fragmentTransaction.detach(fragment);
            }
        };

        abstract void perform(FragmentTransaction fragmentTransaction, int layout,
                              Fragment fragment, String tag);
    }

    private Fragment mFragment;

    private Class mFragmentClass;

    private String mTag;

    private boolean shouldAddToBackStack;

    private boolean shouldPopBackStack;

    private int mLayout;

    private Bundle mArgs;

    private Mode mMode = Mode.REPLACE;

    private int mAnimationStart;

    private int mAnimationEnd;

    private int mPopEnter;

    private int mPopExit;

    public FragmentTransactionBuilder fragment(Fragment fragment) {
        mFragment = fragment;
        return this;
    }

    public FragmentTransactionBuilder fragment(Class fragmentClass) {
        mFragmentClass = fragmentClass;
        return this;
    }

    public FragmentTransactionBuilder tag(String tag) {
        mTag = tag;
        return this;
    }

    public FragmentTransactionBuilder addToBackStack(boolean addToBackStack) {
        shouldAddToBackStack = addToBackStack;
        return this;
    }

    public FragmentTransactionBuilder popBackStack(boolean popBackStack) {
        shouldPopBackStack = popBackStack;
        return this;
    }

    public FragmentTransactionBuilder layoutId(int layout) {
        mLayout = layout;
        return this;
    }

    public FragmentTransactionBuilder args(Bundle args) {
        mArgs = args;
        return this;
    }

    public FragmentTransactionBuilder mode(Mode mode) {
        mMode = mode;
        return this;
    }

    public FragmentTransactionBuilder animations(int animationStart, int animationEnd) {
        mAnimationStart = animationStart;
        mAnimationEnd = animationEnd;
        return this;
    }

    public FragmentTransactionBuilder animations(int animationStart, int animationEnd, int popEnter, int popExit) {
        mPopEnter = popEnter;
        mPopExit = popExit;
        return animations(animationStart, animationEnd);
    }

    String getTag() {
        return getTag(mTag, mFragmentClass);
    }

    public Fragment getFragment(FragmentActivity activity) {
        Fragment fragment = mFragment;
        if (fragment == null) {
            fragment = activity.getSupportFragmentManager().findFragmentByTag(getTag());
        }
        if (fragment == null || !fragment.isVisible()) {
            fragment = getFragment(mFragmentClass);
        }
        return fragment;
    }

    public void execute(FragmentActivity activity) {
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        String tag = getTag();
        Fragment fragment = getFragment(activity);

        if (shouldPopBackStack) {
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        if (mArgs != null) {
            fragment.setArguments(mArgs);
        }

        mMode.perform(transaction, mLayout, fragment, tag);

        if (mAnimationStart != 0 && mAnimationEnd != 0) {
            if (mPopEnter == 0 && mPopExit == 0) {
                transaction.setCustomAnimations(mAnimationStart, mAnimationEnd);
            } else {
                transaction.setCustomAnimations(mAnimationStart, mAnimationEnd, mPopEnter, mPopExit);
            }
        }
        if (shouldAddToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public static Fragment getFragment(Class clazz) {
        try {
            return (Fragment) Class.forName(clazz.getName()).newInstance();
        } catch (InstantiationException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    static String getTag(String tag, Class fragmentClass){
        return (tag != null && !tag.equals("")) ? tag : fragmentClass.getSimpleName();
    }

    public static <FRAGMENT_CLASS> FRAGMENT_CLASS findFragment(Class<FRAGMENT_CLASS> fragmentClass,
                                                               FragmentManager fragmentManager){
        return (FRAGMENT_CLASS) fragmentManager.findFragmentByTag(getTag(null, fragmentClass));
    }

    public static boolean exists(Class fragmentClass, FragmentManager fragmentManager){
        return findFragment(fragmentClass, fragmentManager)!=null;
    }
}
