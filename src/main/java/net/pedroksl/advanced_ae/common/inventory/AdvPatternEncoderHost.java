package net.pedroksl.advanced_ae.common.inventory;

import appeng.api.implementations.menuobjects.ItemMenuHost;
import appeng.menu.locator.ItemMenuHostLocator;
import appeng.util.inv.AppEngInternalInventory;
import appeng.util.inv.InternalInventoryHost;
import com.glodblock.github.extendedae.common.EAESingletons;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.pedroksl.advanced_ae.common.items.AdvPatternEncoderItem;
import net.pedroksl.advanced_ae.gui.patternencoder.AdvPatternEncoderContainer;

public class AdvPatternEncoderHost extends ItemMenuHost<AdvPatternEncoderItem> implements InternalInventoryHost {

	private final AppEngInternalInventory inOutInventory = new AppEngInternalInventory(this, 2);
	private AdvPatternEncoderContainer.inventoryChangedHandler invChangeHandler;

	public AdvPatternEncoderHost(AdvPatternEncoderItem item, Player player, ItemMenuHostLocator locator) {
		super(item, player, locator);

		var itemTag = this.getItemStack().get(EAESingletons.STACK_TAG);
		var registry = player.registryAccess();
		if (itemTag != null) {
			this.inOutInventory.readFromNBT(itemTag, "inOutInventory", registry);
		}
	}

	@Override
	public void saveChangedInventory(AppEngInternalInventory inv) {
		var itemTag = new CompoundTag();
		var registry = this.getPlayer().registryAccess();
		this.inOutInventory.writeToNBT(itemTag, "inOutInventory", registry);
	}

	@Override
	public void onChangeInventory(AppEngInternalInventory inv, int slot) {
		var itemTag = this.getItemStack().getOrDefault(EAESingletons.STACK_TAG, new CompoundTag());
		var registry = this.getPlayer().registryAccess();
		if (this.inOutInventory == inv) {
			this.inOutInventory.writeToNBT(itemTag, "inOutInventory", registry);
		}

		invChangeHandler.handleChange(inv, slot);
	}

	public AppEngInternalInventory getInventory() {
		return this.inOutInventory;
	}

	public void setInventoryChangedHandler(AdvPatternEncoderContainer.inventoryChangedHandler handler) {
		invChangeHandler = handler;
	}
}
