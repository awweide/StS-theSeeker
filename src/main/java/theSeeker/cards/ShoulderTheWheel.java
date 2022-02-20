package theSeeker.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.IronWaveEffect;
import theSeeker.DefaultMod;
import theSeeker.actions.PonderAction;
import theSeeker.actions.ShoulderTheWheelAction;
import theSeeker.characters.TheSeeker;

import static theSeeker.DefaultMod.makeCardPath;

public class ShoulderTheWheel extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(ShoulderTheWheel.class.getSimpleName());
    public static final String IMG = makeCardPath("AsceticStrike.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheSeeker.Enums.COLOR_GRAY;

    private static final int COST = -1;
    private static final int DAMAGE = 4;
    private static final int BLOCK = 4;
    private static final int UPGRADE_PLUS_DMG = 1;
    private static final int UPGRADE_PLUS_BLOCK = 1;

    public ShoulderTheWheel() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        block = baseBlock = BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ShoulderTheWheelAction(p, m, damage, block, damageTypeForTurn, freeToPlayOnce, energyOnUse));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
