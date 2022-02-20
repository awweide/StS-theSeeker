package theSeeker.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.actions.watcher.StanceCheckAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.sun.org.apache.bcel.internal.generic.NEW;
import theSeeker.relics.BedOfNailsRelic;
import theSeeker.stances.ImpetuousStance;
import theSeeker.stances.PensiveStance;

public class PonderAction extends AbstractGameAction {
    private AbstractPlayer p;

    public PonderAction(final AbstractPlayer p) {
        this.p = p;
        actionType = ActionType.SPECIAL;
    }
    
    @Override
    public void update() {
        String currentStance = p.stance.ID;

        if (currentStance == NeutralStance.STANCE_ID) { addToTop(new ChangeStanceAction(new PensiveStance()));
        } else if (currentStance == PensiveStance.STANCE_ID) { addToTop(new ChangeStanceAction(new ImpetuousStance()));
        } else if (currentStance == ImpetuousStance.STANCE_ID) { addToTop(new ChangeStanceAction(new NeutralStance()));
        } else { addToTop(new ChangeStanceAction(new NeutralStance()));
        }

        isDone = true;
    }
}
