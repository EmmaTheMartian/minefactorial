package martian.minefactorial.client.screen;

import martian.minefactorial.api.client.screen.AbstractMachineScreen;
import martian.minefactorial.content.block.machinery.husbandry.BlockChronotyperBE;
import martian.minefactorial.content.menu.ContainerChronotyper;
import martian.minefactorial.content.net.PacketServerboundSetChronotyperState;
import martian.minefactorial.content.net.PacketServerboundSetSmasherFortuneLevel;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

import static martian.minefactorial.Minefactorial.id;

public class ScreenChronotyper extends AbstractMachineScreen<BlockChronotyperBE, ContainerChronotyper> {
    public static final ResourceLocation UI = id("textures/gui/blank_machine.png");

    private Button stateButton;

    public ScreenChronotyper(ContainerChronotyper menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        stateButton = ImageButton
                .builder(
                    menu.getMoveState() == BlockChronotyperBE.MoveState.BABIES ?
                            Component.translatable("screen.minefactorial.chronotyper.state_babies") :
                            Component.translatable("screen.minefactorial.chronotyper.state_adults"),
                    btn -> {
                        switch (menu.getMoveState()) {
                            case BABIES -> {
                                btn.setMessage(Component.translatable("screen.minefactorial.chronotyper.state_adults"));
                                menu.setMoveState(BlockChronotyperBE.MoveState.ADULTS);
                            }
                            case ADULTS -> {
                                btn.setMessage(Component.translatable("screen.minefactorial.chronotyper.state_babies"));
                                menu.setMoveState(BlockChronotyperBE.MoveState.BABIES);
                            }
                        }
                        PacketDistributor.sendToServer(new PacketServerboundSetChronotyperState(menu.getMoveState().ordinal()));
                    })
                .pos(leftPos + 12, topPos + 35)
                .size(Button.DEFAULT_WIDTH, Button.DEFAULT_HEIGHT)
                .createNarration(Supplier::get)
                .build();

        addRenderableWidget(stateButton);
    }

    @Override
    protected ResourceLocation getUI() {
        return UI;
    }

    @Override
    public void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(graphics, partialTick, mouseX, mouseY);

        stateButton.render(graphics, mouseX, mouseY, partialTick);
    }
}
