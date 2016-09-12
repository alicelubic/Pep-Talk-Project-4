package owlslubic.peptalkapp;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by owlslubic on 9/11/16.
 */
public class FloatingActionsMenuBehavior extends CoordinatorLayout.Behavior<FloatingActionsMenu>{

        public FloatingActionsMenuBehavior(Context context, AttributeSet attributeSet){}

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionsMenu child, View dependency) {
            return dependency instanceof Snackbar.SnackbarLayout;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionsMenu child, View dependency) {
            float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
            child.setTranslationY(translationY);
            return true;
        }
    }


