package theSeeker.stances;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.badlogic.gdx.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.badlogic.gdx.math.*;
import com.megacrit.cardcrawl.core.*;
import com.badlogic.gdx.graphics.*;
import com.megacrit.cardcrawl.vfx.*;
import com.megacrit.cardcrawl.vfx.stance.*;
import theSeeker.DefaultMod;
import theSeeker.powers.InspirationPower;
import theSeeker.relics.ApocryphaRelic;

public class PensiveStance extends AbstractStance
{
    public static final String STANCE_ID = DefaultMod.makeID(PensiveStance.class.getSimpleName());
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
    private static long sfxId = -1L;

    public PensiveStance() {
        this.ID = STANCE_ID;
        this.name = stanceString.NAME;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = PensiveStance.stanceString.DESCRIPTION[0];
    }

    @Override
    public void onExitStance() {
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
        //Hard-coded logic for ApocryphaRelic
        if (AbstractDungeon.player.hasRelic(ApocryphaRelic.ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new InspirationPower(AbstractDungeon.player, 1), 1));
        }
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
        if (PensiveStance.sfxId != -1L) {
            this.stopIdleSfx();
        }
        CardCrawlGame.sound.play("POWER_DEXTERITY");
        PensiveStance.sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_CALM");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.LIME, true));
        AbstractDungeon.effectsQueue.add(new StanceChangeParticleGenerator(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, "Wrath"));
    }

    @Override
    public void stopIdleSfx() {
        if (PensiveStance.sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_CALM", PensiveStance.sfxId);
            PensiveStance.sfxId = -1L;
        }
    }
}
