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

public class PlayChosenConditionalCardAction extends AbstractGameAction {
    private AbstractPlayer player;

    //TODO: Gridselect string hard-coded

    // Variables to determine card logic
    private String fromCardPile;
    private AbstractCard.CardType cardType;
    private int cardCost;
    private AbstractCard cardToPlay;
    private boolean forceExhaust;
    private boolean forceUnexhaust;
    private CardGroup cardPile;
    private String TEXT = "Choose a card to play";

    public PlayChosenConditionalCardAction(AbstractCreature target, String fromCardPile, AbstractCard.CardType cardType, int cardCost, boolean forceExhaust, boolean forceUnexhaust) {
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

    public PlayChosenConditionalCardAction(AbstractCreature target, String fromCardPile, AbstractCard.CardType cardType, int cardCost) {
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
            CardGroup eligibleCards = new CardGroup(CardGroupType.UNSPECIFIED);
            while ((cardPileIterator != null) && (cardPileIterator.hasNext())) {
                AbstractCard c = (AbstractCard) cardPileIterator.next();
                if (c.type == this.cardType) {
                    if ((this.cardCost < 0) || (c.costForTurn == this.cardCost)) {
                        eligibleCards.addToTop(c);
                    }
                }
            }

            if (eligibleCards.isEmpty()) {
                this.isDone = true;
            } else {
                // Prompt selection of card to play
                eligibleCards.sortAlphabetically(true);
                eligibleCards.sortByRarityPlusStatusCardType(false);
                AbstractDungeon.gridSelectScreen.open(eligibleCards, 1, TEXT, false);
                this.tickDuration();
            }
        } else {
            if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                Iterator var1 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();

                while(var1.hasNext()) {
                    AbstractCard c = (AbstractCard)var1.next();
                    c.exhaust = true;
                    AbstractDungeon.player.drawPile.group.remove(c);
                    AbstractDungeon.getCurrRoom().souls.remove(c);
                    this.addToBot(new NewQueueCardAction(c, true, false, true));
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }
            this.tickDuration();
        }
    }
}
