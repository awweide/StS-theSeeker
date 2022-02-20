package theSeeker.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theSeeker.DefaultMod;
import theSeeker.characters.TheSeeker;
import theSeeker.powers.InspirationPower;

import static theSeeker.DefaultMod.makeCardPath;

public class Eminence extends AbstractDynamicCard {

    /*
     * Eminence: Gain 6 (10) block. Double your Inspiration.
     */

    public static final String ID = DefaultMod.makeID(Eminence.class.getSimpleName());
    // TODO: Image
    public static final String IMG = makeCardPath("AsceticStrike.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheSeeker.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int BLOCK = 6;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public Eminence() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseBlock = BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(p, p, block));
        AbstractPower playerInspirationPower = p.getPower(InspirationPower.POWER_ID);
        if ((playerInspirationPower != null) && (playerInspirationPower.amount > 0)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p,
                    new InspirationPower(p, playerInspirationPower.amount), playerInspirationPower.amount));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
