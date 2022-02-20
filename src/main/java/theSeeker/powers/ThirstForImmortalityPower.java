package theSeeker.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.FairyPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theSeeker.DefaultMod;
import theSeeker.util.TextureLoader;

import java.util.ArrayList;

import static theSeeker.DefaultMod.makePowerPath;

public class ThirstForImmortalityPower extends AbstractPower {
    public static final String POWER_ID = DefaultMod.makeID(ThirstForImmortalityPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    ArrayList<Integer> temporarySlotIndices;

    public ThirstForImmortalityPower(final AbstractCreature owner, final int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 0;

        type = PowerType.BUFF;
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        // Responsible for tracking temporary potion slots to delete them later
        temporarySlotIndices = new ArrayList<Integer>();
        stackPower(amount);
        updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        while ((amount < 3) && (stackAmount > 0)) {
            AbstractPotion potionToObtain = new FairyPotion();
            amount += 1;
            stackAmount -= 1;
            int newSlotPosition = AbstractDungeon.player.potionSlots;
            AbstractDungeon.player.potionSlots += 1;
            AbstractDungeon.player.potions.add(new PotionSlot(newSlotPosition));
            AbstractDungeon.player.obtainPotion(newSlotPosition, potionToObtain);
            potionToObtain.flash();
            AbstractPotion.playPotionSound();
            temporarySlotIndices.add(newSlotPosition);
        }
    }

    @Override
    public void reducePower(int stackAmount) {
        // Remove temporary potion slots when unstacking power
        while ((stackAmount > 0) && (amount > 0)) {
            amount -= 1;
            stackAmount -= 1;
            AbstractDungeon.player.potionSlots -= 1;
            int potionsIndexToRemove = temporarySlotIndices.get(temporarySlotIndices.size() - 1);
            temporarySlotIndices.remove(temporarySlotIndices.size() - 1);
            AbstractDungeon.player.potions.remove(potionsIndexToRemove);
        }
        if (amount <= 0) {
            addToTop(new RemoveSpecificPowerAction(owner, owner, ThirstForImmortalityPower.POWER_ID));
        }
    }

    // At end of battle, explicitly remove power to get rid of temporary potions and slots
    @Override
    public void onVictory() {reducePower(amount);}

    @Override
    public void updateDescription() {
        if (this.amount == 1){
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }
    }

}
