package theSeeker.stances;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.ShineSparkleEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceChangeParticleGenerator;
import jdk.nashorn.internal.ir.BlockLexicalContext;
import theSeeker.DefaultMod;

public class NihilismStance extends AbstractStance
{
    public static final String STANCE_ID = DefaultMod.makeID(NihilismStance.class.getSimpleName());
    private static final StanceStrings stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
    private static long sfxId = -1L;
    private static final int BLOCK_ON_EXIT = 6;

    public NihilismStance() {
        this.ID = STANCE_ID;
        this.name = stanceString.NAME;
        this.updateDescription();

    }

    @Override
    public void updateDescription() {
        this.description = NihilismStance.stanceString.DESCRIPTION[0] + BLOCK_ON_EXIT + NihilismStance.stanceString.DESCRIPTION[1];
    }

    @Override
    public void onExitStance() {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, BLOCK_ON_EXIT));
        AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new NihilismStance()));
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
        if (NihilismStance.sfxId != -1L) {
            this.stopIdleSfx();
        }
        CardCrawlGame.sound.play("POWER_DEXTERITY");
        NihilismStance.sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_CALM");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.LIME, true));
        AbstractDungeon.effectsQueue.add(new StanceChangeParticleGenerator(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, "Wrath"));
    }

    @Override
    public void stopIdleSfx() {
        if (NihilismStance.sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_CALM", NihilismStance.sfxId);
            NihilismStance.sfxId = -1L;
        }
    }
}
