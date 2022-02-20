package theSeeker.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theSeeker.relics.BedOfNailsRelic;

public class PrefaceAction extends AbstractGameAction {
    private AbstractPlayer p;
    private int magicNumber;

    private static final int MAGIC = 1;

    public PrefaceAction(final AbstractPlayer p) {
        this.p = p;
        this.magicNumber = MAGIC;
        actionType = ActionType.ENERGY;
    }
    
    @Override
    public void update() {
        int cardsPlayedThisTurn = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
        if ((cardsPlayedThisTurn == 1) || ((cardsPlayedThisTurn == 2) && (p.hasRelic(BedOfNailsRelic.ID)))) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.magicNumber));
            //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.p, this.p, new InspirationPower(this.p, magicNumber), magicNumber));
        }
        isDone = true;
    }
}
