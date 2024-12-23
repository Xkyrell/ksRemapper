package me.xkyrell.ksremapper;

import me.xkyrell.ksremapper.annotation.*;
import java.util.Date;

import static me.xkyrell.ksremapper.annotation.RemapField.Mode.*;

@RemapClass(User.class) // or @RemapTarget("me.xkyrell.ksremapper.User")
public interface UserProxy extends RemapProxy {

    @RemapField(isStatic = true, mode = GET)
    int ID();

    @RemapField(mode = SET)
    void name(String name);

    @RemapTarget("j")
    @RemapField(mode = SET)
    void setDate(Date date);

    @RemapTarget("nC")
    @RemapMethod(isStatic = true)
    void notifyCrash();

    @RemapMethod
    Date getJoined();

    @RemapMethod
    String getName();

    @RemapMethod
    void setPoints(int points);

    @RemapMethod
    int getPoints();

    @RemapMethod
    void setTag(String tag);

    @RemapMethod
    String getTag();

    default void setUnlimitedPoints() {
        setPoints(Integer.MAX_VALUE);
    }

}
