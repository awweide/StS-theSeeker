package theSeeker.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theSeeker.DefaultMod;
import theSeeker.relics.TinPeacockRelic;
import theSeeker.util.TextureLoader;

import static theSeeker.DefaultMod.makePowerPath;

public class ImpetuousPower extends AbstractPower {
    public static final String POWER_ID = DefaultMod.makeID(ImpetuousPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    private static final int ATTACKS_TO_TRIGGER = 2;

    public ImpetuousPower(AbstractCreature owner, int attacksLeftToTrigger) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = attacksLeftToTrigger;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    public ImpetuousPower(AbstractCreature owner) {
        this(owner, ATTACKS_TO_TRIGGER);
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[1];
        } else if (amount >= 2) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        } else if (amount <= 0) {
            description = DESCRIPTIONS[0] + DESCRIPTIONS[3];
        }
    }

    @Override
    public void atStartOfTurn() {
        amount = ATTACKS_TO_TRIGGER;
        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if ((card.type == AbstractCard.CardType.ATTACK) && (amount > 0)) {
            --amount;
            updateDescription();
        }
        if ((card.type == AbstractCard.CardType.ATTACK) && (amount == 0)) {
            this.flash();
            AbstractMonster m = null;
            if (action.target != null) {
                m = (AbstractMonster)action.target;
            }

            AbstractCard tmp = card.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = (float)Settings.HEIGHT / 2.0F;
            if (m != null) {
                tmp.calculateCardDamage(m);
            }

            tmp.purgeOnUse = true;
            AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);

            if (AbstractDungeon.player.hasRelic(TinPeacockRelic.ID)) {
                amount = ATTACKS_TO_TRIGGER;
                updateDescription();
            } else {
                amount = -1;
                updateDescription();
                addToBot(new RemoveSpecificPowerAction(owner, owner, this.ID));
            }
        }

    }
}
