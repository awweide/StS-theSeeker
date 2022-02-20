package theSeeker.cards;

import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.ExhumeAction;
import com.megacrit.cardcrawl.actions.unique.TransmutationAction;
import com.megacrit.cardcrawl.actions.unique.TransmuteAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import theSeeker.DefaultMod;
import theSeeker.actions.ModifyMagicNumberAction;
import theSeeker.actions.ShoulderTheWheelAction;
import theSeeker.characters.TheSeeker;
import theSeeker.powers.DominionPower;

import java.util.ArrayList;
import java.util.Iterator;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;
import static theSeeker.DefaultMod.makeCardPath;

public class ProveByInduction extends AbstractDynamicCard {

    public static final String ID = DefaultMod.makeID(ProveByInduction.class.getSimpleName());
    public static final String IMG = makeCardPath("AsceticStrike.png");

    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheSeeker.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int MAGIC = 3;
    //private static final int TRANSFORM_REQ = 3;

    public ProveByInduction() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MAGIC;
        cardsToPreview = new QuodEratDemonstrandum();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public void triggerExhaustedCardsOnStanceChange(AbstractStance newStance) {
        if (magicNumber > 1) {
            addToBot(new DiscardToHandAction(this));
            addToBot(new ModifyMagicNumberAction(this.uuid, -1));
        }
        else {
            addToBot(new MakeTempCardInHandAction(new QuodEratDemonstrandum()));
            //player.discardPile.removeCard(this);
            addToBot(new ExhaustSpecificCardAction(this, player.discardPile));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
