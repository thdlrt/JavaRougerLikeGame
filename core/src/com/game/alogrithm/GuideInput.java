package com.game.alogrithm;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.game.io.NetWork;
import com.game.screen.GuideScreen;

public class GuideInput extends InputListener {
    GuideScreen guest;
    public GuideInput(GuideScreen guest) {
        this.guest = guest;
    }

}
