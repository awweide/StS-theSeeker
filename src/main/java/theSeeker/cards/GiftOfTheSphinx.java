package theSeeker.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theSeeker.DefaultMod;
import theSeeker.actions.PlayChosenConditionalCardAction;
import theSeeker.characters.TheSeeker;
import theSeeker.powers.InspirationPower;

import static theSeeker.DefaultMod.makeCardPath;

public class GiftOfTheSphinx extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(GiftOfTheSphinx.class.getSimpleName());
    public static final String IMG = makeCardPath("AsceticStrike.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheSeeker.Enums.COLOR_GRAY;

    private static final int COST = 4;
    private static final int UPGRADE_COST = 3;

    public GiftOfTheSphinx() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PlayChosenConditionalCardAction(p, "DRAW", CardType.POWER, -1));
        addToBot(new PlayChosenConditionalCardAction(p, "DRAW", CardType.SKILL, -1));
        addToBot(new PlayChosenConditionalCardAction(p, "DRAW", CardType.ATTACK, -1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            initializeDescription();
        }
    }
}
