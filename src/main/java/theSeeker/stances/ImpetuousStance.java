package theSeeker.stances;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.ShineSparkleEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceChangeParticleGenerator;
import theSeeker.DefaultMod;
import theSeeker.powers.ImpetuousPower;

public class ImpetuousStance extends AbstractStance
{
    public static final String STANCE_ID = DefaultMod.makeID(ImpetuousStance.class.getSimpleName());
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
    private static long sfxId = -1L;
    private static final int ATTACKS_TO_TRIGGER = 2;

    public ImpetuousStance() {
        this.ID = STANCE_ID;
        this.name =  stanceString.NAME;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = ImpetuousStance.stanceString.DESCRIPTION[0];
    }

    @Override
    public void onExitStance() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, ImpetuousPower.POWER_ID));
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * 1.5F : damage;
    }

    @Override
    public void updateAnimation() {
        if (!Settings.DISABLE_EFFECTS) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0f) {
                this.particleTimer = 0.05f;
                //AbstractDungeon.effectsQueue.add(new WrathParticleEffect());
                AbstractDungeon.effectsQueue.add(new ShineSparkleEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-AbstractDungeon.player.hb.width / 2.0f - 30.0f * Settings.scale, AbstractDungeon.player.hb.width / 2.0f + 30.0f * Settings.scale), AbstractDungeon.player.hb.cY + MathUtils.random(-AbstractDungeon.player.hb.height / 2.0f - -10.0f * Settings.scale, AbstractDungeon.player.hb.height / 2.0f - 10.0f * Settings.scale)));
            }
        }
        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0f) {
            this.particleTimer2 = MathUtils.random(0.3f, 0.4f);
            AbstractDungeon.effectsQueue.add(new ReplayStanceAuraEffect(STANCE_ID));
        }
    }

    @Override
    public void onEnterStance() {
        if (ImpetuousStance.sfxId != -1L) {
            this.stopIdleSfx();
        }
        CardCrawlGame.sound.play("POWER_STRENGTH");
        ImpetuousStance.sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_WRATH");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.LIME, true));
        AbstractDungeon.effectsQueue.add(new StanceChangeParticleGenerator(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, "Wrath"));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ImpetuousPower(AbstractDungeon.player, ATTACKS_TO_TRIGGER), ATTACKS_TO_TRIGGER, true));
    }

    @Override
    public void stopIdleSfx() {
        if (ImpetuousStance.sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_WRATH", ImpetuousStance.sfxId);
            ImpetuousStance.sfxId = -1L;
        }
    }
}
