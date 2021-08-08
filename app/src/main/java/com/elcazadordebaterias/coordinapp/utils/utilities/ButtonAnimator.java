package com.elcazadordebaterias.coordinapp.utils.utilities;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.elcazadordebaterias.coordinapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ButtonAnimator {

    // Animations
    private final Animation rotateOpen;
    private final Animation rotateClose;
    private final Animation fromBottom;
    private final Animation toBottom;

    // Boolean to check if the main button is clicked
    private boolean clicked;

    // Main button
    private final FloatingActionButton mainButton;

    // Array with the buttons of the menu
    private final ArrayList<FloatingActionButton> buttons;

    public ButtonAnimator(Context context, FloatingActionButton mainButton, ArrayList<FloatingActionButton> buttons){
        this.mainButton = mainButton;
        this.buttons = buttons;

        clicked = false;

        for (FloatingActionButton button : buttons){
            button.setVisibility(View.GONE);
        }

        rotateOpen = AnimationUtils.loadAnimation(context, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(context, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(context, R.anim.from_botton_anim);
        toBottom = AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim);
    }

    public void onButtonClicked(){
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    private void setVisibility(boolean clicked){
        if(!clicked){
            for (FloatingActionButton button : buttons){
                button.setVisibility(View.VISIBLE);
            }
        } else {
            for (FloatingActionButton button : buttons){
                button.setVisibility(View.GONE);
            }
        }
    }

    private void setAnimation(boolean clicked){
        if(!clicked) {
            for (FloatingActionButton button : buttons){
                button.startAnimation(fromBottom);
            }
            mainButton.startAnimation(rotateOpen);
        } else {
            for (FloatingActionButton button : buttons){
                button.startAnimation(toBottom);
            }
            mainButton.startAnimation(rotateClose);
        }
    }

}
