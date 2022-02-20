package theSeeker.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theSeeker.DefaultMod;
import theSeeker.util.TextureLoader;

import static theSeeker.DefaultMod.makePowerPath;

public class DazzledPower extends AbstractPower {
    public static final String POWER_ID = DefaultMod.makeID(DazzledPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private final float PER_STACK_DAMAGE_FACTOR = 0.90f;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    public DazzledPower(final AbstractCreature owner, final int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        type = PowerType.DEBUFF;
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        float damageMultiplier = 1.0f;
        for (int i = 0; i < this.amount; i++) {
            damageMultiplier *= PER_STACK_DAMAGE_FACTOR;
        }
        return type == DamageInfo.DamageType.NORMAL ? damage * damageMultiplier : damage;
    }

    @Override
    public void atStartOfTurn() {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, DazzledPower.POWER_ID));
    }

    @Override
    public void updateDescription() {
        float damageMultiplier = 1.0f;
        for (int i = 0; i < this.amount; i++) {
            damageMultiplier *= PER_STACK_DAMAGE_FACTOR;
        }
        int percentage = (int) ((1.0f - damageMultiplier) * 100);
        description = DESCRIPTIONS[0] + percentage + DESCRIPTIONS[1];
    }
}
