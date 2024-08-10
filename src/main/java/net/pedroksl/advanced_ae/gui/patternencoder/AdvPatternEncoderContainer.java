package net.pedroksl.advanced_ae.gui.patternencoder;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.inventories.InternalInventory;
import appeng.api.stacks.AEKey;
import appeng.crafting.pattern.AEProcessingPattern;
import appeng.menu.AEBaseMenu;
import appeng.menu.SlotSemantics;
import appeng.menu.implementations.MenuTypeBuilder;
import appeng.menu.slot.OutputSlot;
import appeng.menu.slot.RestrictedInputSlot;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.pedroksl.advanced_ae.common.inventory.AdvPatternEncoderInventory;
import net.pedroksl.advanced_ae.common.patterns.AdvProcessingPattern;

import java.util.HashMap;

public class AdvPatternEncoderContainer extends AEBaseMenu {
	public static final MenuType<AdvPatternEncoderContainer> TYPE = MenuTypeBuilder
			.create(AdvPatternEncoderContainer::new, AdvPatternEncoderInventory.class)
			.build("adv_pattern_encoder");

	private final RestrictedInputSlot inputSlot;
	private final OutputSlot outputSlot;
	private final AdvPatternEncoderInventory host;

	private AdvPatternEncoderGui childGui;

	public AdvPatternEncoderContainer(int id, Inventory playerInventory, AdvPatternEncoderInventory host) {
		super(TYPE, id, playerInventory, host);
		this.createPlayerInventorySlots(playerInventory);
		this.host = host;

		this.addSlot(this.inputSlot = new RestrictedInputSlot(RestrictedInputSlot.PlacableItemType.ENCODED_PATTERN,
				host.getInventory(), 0), SlotSemantics.ENCODED_PATTERN);
		this.addSlot(this.outputSlot = new OutputSlot(host.getInventory(), 1, null), SlotSemantics.MACHINE_OUTPUT);

		host.setInventoryChangedHandler(this::onChangeInventory);
	}

	public void onChangeInventory(InternalInventory inv, int slot) {
		if (inv == host.getInventory()) {
			if (slot == 0) {
				if (this.inputSlot.hasItem()) {
					decodeInputPattern();
				} else {
					clearDecodedPattern();
				}
			} else if (slot == 1 && !this.outputSlot.hasItem()) {
				this.inputSlot.set(ItemStack.EMPTY);
			}
		}
	}

	private void decodeInputPattern() {
		ItemStack stack = this.inputSlot.getItem();

		IPatternDetails details = PatternDetailsHelper.decodePattern(stack, this.getPlayer().level(), false);

		if (details == null) return;

		if (!(details instanceof AEProcessingPattern pattern)) return;
		boolean advPattern = details instanceof AdvProcessingPattern;
		AdvProcessingPattern advDetails = advPattern ? (AdvProcessingPattern) details :
				null;

		var sparceInputs = pattern.getSparseInputs();

		HashMap<AEKey, Direction> inputList = new HashMap<>();
		for (int x = 0; x < sparceInputs.length; x++) {
			var input = sparceInputs[x];
			if (input == null) {
				continue;
			}

			if (!inputList.containsKey(input.what())) {
				Direction dir = advPattern ? advDetails.getDirectionSideForInputSlot(x) : null;
				inputList.put(input.what(), dir);
			}
		}

		childGui.refreshList(inputList);
	}

	private void clearDecodedPattern() {
	}

	public void update() {

	}

	protected void setChild(AdvPatternEncoderGui child) {
		this.childGui = child;
	}

	public interface inventoryChangedHandler {
		public void handleChange(InternalInventory inv, int slot);
	}
}
