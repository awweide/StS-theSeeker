package theSeeker.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import javax.smartcardio.Card;
import java.util.ArrayList;
import java.util.Iterator;

public class PlayRandomConditionalCardAction extends AbstractGameAction {
    private AbstractPlayer player;

    // Variables to determine card logic
    private String fromCardPile;
    private AbstractCard.CardType cardType;
    private int cardCost;
    private AbstractCard cardToPlay;
    private boolean forceExhaust;
    private boolean forceUnexhaust;
    private CardGroup cardPile;

    public PlayRandomConditionalCardAction(AbstractCreature target, String fromCardPile, AbstractCard.CardType cardType, int cardCost, boolean forceExhaust, boolean forceUnexhaust) {
        this.target = target;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;

        this.fromCardPile = fromCardPile;
        this.cardType = cardType;
        this.cardCost = cardCost;
        this.forceExhaust = forceExhaust;
        this.forceUnexhaust = forceUnexhaust;

        this.cardToPlay = null;
        this.cardPile = null;
    }

    public PlayRandomConditionalCardAction(AbstractCreature target, String fromCardPile, AbstractCard.CardType cardType, int cardCost) {
        this(target, fromCardPile, cardType, cardCost, false, false);
    }

    public void update() {
        if (this.duration == this.startDuration) {

            // Setup conditionals
            if (this.fromCardPile == "DRAW") { this.cardPile = this.player.drawPile; }
            else if (this.fromCardPile == "DISCARD") { this.cardPile = this.player.discardPile; }
            else if (this.fromCardPile == "EXHAUST") { this.cardPile = this.player.exhaustPile; }

            Iterator cardPileIterator = (this.cardPile == null) ? null : this.cardPile.group.iterator() ;

            // Find cards satisfying conditionals
            ArrayList<AbstractCard> eligibleCards = new ArrayList();
            while ((cardPileIterator != null) && (cardPileIterator.hasNext())) {
                AbstractCard c = (AbstractCard) cardPileIterator.next();
                if (c.type == this.cardType) {
                    if ((this.cardCost < 0) || (c.costForTurn == this.cardCost)) {
                        eligibleCards.add(c);
                    }
                }
            }

            // Set card to play
            if (!eligibleCards.isEmpty()) {
                this.cardToPlay = eligibleCards.get(AbstractDungeon.miscRng.random(0, eligibleCards.size() - 1));
            }

            // If no card found, action is done; else, allow action to proceed to else next else block
            if (this.cardToPlay == null) { this.isDone = true; }
            else { this.tickDuration(); }
        } else {
            if (this.cardToPlay != null) {
                // If card is found, play it

                // Force exhausts - buggy implementation since exhaust settings linger
                if (this.forceExhaust) { this.cardToPlay.exhaust = true; }
                if (this.forceUnexhaust) {this.cardToPlay.exhaust = false; }

                this.cardPile.group.remove(this.cardToPlay);
                AbstractDungeon.getCurrRoom().souls.remove(this.cardToPlay);

                // If this.target

                boolean thisActionHasMeaningfulTarget = ((this.target != null) && (this.target != this.player));
                boolean thisActionHasLivingTarget = !(this.target.isDying) && !(this.target.currentHealth <= 0) && !(this.target.halfDead);
                boolean cardToPlayHasMeaningfulTarget = ((this.cardToPlay.target == AbstractCard.CardTarget.ENEMY) || (this.cardToPlay.target == AbstractCard.CardTarget.SELF_AND_ENEMY));
                if (thisActionHasMeaningfulTarget && thisActionHasLivingTarget && cardToPlayHasMeaningfulTarget) {
                    this.addToBot(new NewQueueCardAction(this.cardToPlay, this.target, false, true));
                } else {
                    this.addToBot(new NewQueueCardAction(this.cardToPlay, true, false, true));
                }
             }
            this.tickDuration();
        }
    }
}
