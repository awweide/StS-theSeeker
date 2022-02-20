package theSeeker.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theSeeker.DefaultMod;
import theSeeker.actions.PonderAction;
import theSeeker.characters.TheSeeker;
import theSeeker.stances.ImpetuousStance;

import static theSeeker.DefaultMod.makeCardPath;

public class Rabidity extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(Rabidity.class.getSimpleName());
    // TODO: Image
    public static final String IMG = makeCardPath("AsceticStrike.png");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheSeeker.Enums.COLOR_GRAY;

    private static final int COST = 2;

    public Rabidity() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ChangeStanceAction(new ImpetuousStance()));
        this.addToBot(new DrawCardAction(1));
        if (upgraded) {
            this.addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public void triggerOnCardPlayed(AbstractCard c) {
        if ((c.costForTurn >= 2) && (costForTurn != 0)) {
            setCostForTurn(0);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
