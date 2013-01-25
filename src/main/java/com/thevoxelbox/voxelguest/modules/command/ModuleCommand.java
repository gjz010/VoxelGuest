package com.thevoxelbox.voxelguest.modules.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author keto23
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ModuleCommand {
    
    /**
     *
     * @return Name of the command
     */
    String name();
    
    /**
     *
     * @return Aliases of the command
     */
    String[] aliases() default {};
    
    /**
     *
     * @return The permission needed to run the command
     */
    String permission() default "";
    
    /**
     *
     * @return If the command is only able to be run by players
     */
    boolean playerOnly() default false;
    
    /**
     *
     * @return Help for the command
     */
    String help() default "§cNo help provided for this command!";
    
}
