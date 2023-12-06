package com.veeonthewall.tabby.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// Allows you to convert a string formatted message with color codes to a Component that can be sent to a player
public class MessageUtil {
    public static Component createColoredMessage(String message) {
        return LegacyComponentSerializer.builder()
                .hexColors()
                .useUnusualXRepeatedCharacterHexFormat()
                .build()
                .deserialize(message.replace("&", "§"));
    }

    // Method to just convert & color codes to § color codes, while maintaining hex codes
    public static String convertColorCodes(String message) {
        return message.replace("&", "§");
    }
}