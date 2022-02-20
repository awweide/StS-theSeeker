package theSeeker.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.unique.TransmutationAction;
import com.megacrit.cardcrawl.actions.unique.TransmuteAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theSeeker.DefaultMod;
import theSeeker.actions.RewriteAction;
import theSeeker.characters.TheSeeker;
import theSeeker.powers.ArrogancePower;

import static theSeeker.DefaultMod.makeCardPath;

public class Rewrite extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(Rewrite.class.getSimpleName());
    // TODO: Image
    public static final String IMG = makeCardPath("AsceticStrike.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheSeeker.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int BLOCK = 8;
    private static final int UPGRADE_BLOCK = 3;

    public Rewrite() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        cardsToPreview = new Draft();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RewriteAction(cardsToPreview));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
            initializeDescription();
        }
    }
}
