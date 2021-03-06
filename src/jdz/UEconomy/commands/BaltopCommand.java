
package jdz.UEconomy.commands;

import static org.bukkit.ChatColor.*;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jdz.UEconomy.UEcoFormatter;
import jdz.UEconomy.data.UEcoEntry;
import jdz.UEconomy.data.UEcoTop;
import jdz.bukkitUtils.commands.Command;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandMethod;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

@CommandLabel("baltop")
public class BaltopCommand extends Command {
	private static final int ITEMS_PER_PAGE = 10;

	@CommandMethod
	public void showTop(CommandSender sender) {
		showTop(sender, 1);
	}

	@CommandMethod
	public void showTop(CommandSender sender, int page) {
		int pageIndex = page - 1;
		if (pageIndex < 0)
			pageIndex = 0;

		int maxPages = Math.max(0, UEcoTop.getTop().size() / ITEMS_PER_PAGE);
		if (pageIndex > maxPages)
			pageIndex = maxPages;

		List<String> messages = new ArrayList<String>();

		messages.add(GRAY + "======== " + GOLD + "Baltop " + GREEN + "Page " + (pageIndex + 1) + "/" + (maxPages + 1)
				+ GRAY + " =======");

		if (sender instanceof Player) {
			int index = UEcoTop.getPosition(sender.getName());
			if (index != -1) {
				messages.add(getLine(index));
				messages.add("");
			}
		}

		int startIndex = pageIndex * ITEMS_PER_PAGE;
		int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, UEcoTop.getTop().size());

		for (int i = startIndex; i < endIndex; i++)
			messages.add(getLine(i));

		sender.sendMessage(messages.toArray(new String[messages.size()]));
		sender.spigot().sendMessage(getNavigationLine(pageIndex, maxPages));
	}

	private String getLine(int index) {
		UEcoEntry entry = UEcoTop.getTop().get(index);
		String s = GOLD + "[" + (index + 1) + "] " + GREEN + entry.getName() + WHITE + " $"
				+ UEcoFormatter.charFormat(entry.getBalance(), 9);
		return s;
	}

	private TextComponent getNavigationLine(int pageIndex, int maxPages) {
		TextComponent border = new TextComponent(GRAY + "========");
		TextComponent previous, next;
		if (pageIndex > 0) {
			previous = new TextComponent(GOLD + "[<<<<]");
			previous.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/baltop " + (pageIndex - 1)));
		}
		else
			previous = new TextComponent(GRAY+"========");

		if (pageIndex < maxPages) {
			next = new TextComponent(GOLD + " [>>>>] ");
			next.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/baltop " + (pageIndex + 1)));
		}
		else
			next = new TextComponent(GRAY+"========");

		return new TextComponent(border, previous, next, border);
	}
}
