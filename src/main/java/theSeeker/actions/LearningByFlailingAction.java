package theSeeker.actions;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import theSeeker.cardModifiers.ExperienceModifier;

import java.util.ArrayList;
import java.util.Iterator;

public class LearningByFlailingAction extends AbstractGameAction {
    private DamageInfo info;
    private AbstractCard theStrike = null;
    private AbstractCard theDefend = null;

    public LearningByFlailingAction(AbstractCreature target, DamageInfo info, int amount) {
        this.amount = amount;
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_MED;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED && this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.NONE));
            this.target.damage(this.info);
            if ((((AbstractMonster)this.target).isDying || this.target.currentHealth <= 0) && !this.target.halfDead && !this.target.hasPower("Minion")) {

                ArrayList<AbstractCard> possibleStrikes = new ArrayList();
                ArrayList<AbstractCard> possibleDefends = new ArrayList();
                Iterator var2 = AbstractDungeon.player.masterDeck.group.iterator();

                while(var2.hasNext()) {
                    AbstractCard c = (AbstractCard)var2.next();
                    if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
                        possibleStrikes.add(c);
                    }
                    if (c.hasTag(AbstractCard.CardTags.STARTER_DEFEND)) {
                        possibleDefends.add(c);
                    }
                }

                if (!possibleStrikes.isEmpty()) {
                    this.theStrike = (AbstractCard)possibleStrikes.get(AbstractDungeon.miscRng.random(0, possibleStrikes.size() - 1));
                    CardModifierManager.addModifier(this.theStrike, new ExperienceModifier(1));
                }
                if (!possibleDefends.isEmpty()) {
                    this.theDefend = (AbstractCard)possibleDefends.get(AbstractDungeon.miscRng.random(0, possibleDefends.size() - 1));
                    CardModifierManager.addModifier(this.theDefend, new ExperienceModifier(1));
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration();
        if (this.isDone) {
            if (this.theStrike != null) {
                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(this.theStrike.makeStatEquivalentCopy()));
                this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
            }
            if (this.theDefend != null) {
                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(this.theDefend.makeStatEquivalentCopy()));
                this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
            }
        }

    }
}
