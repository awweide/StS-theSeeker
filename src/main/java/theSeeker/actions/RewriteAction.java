package theSeeker.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
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

public class RewriteAction extends AbstractGameAction {
    private AbstractPlayer player;

    //TODO: Gridselect string hard-coded

    // Variables to determine card logic
    private AbstractCard cardToReplace;
    private AbstractCard cardToReplaceWith;
    private String TEXT = "Choose a card to exhaust";

    public RewriteAction(AbstractCard cardToReplaceWith) {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;

        this.cardToReplaceWith = cardToReplaceWith;
    }

    public void update() {
        if (this.duration == this.startDuration) {

            // Setup conditionals
            Iterator cardPileIterator = (player.drawPile == null) ? null : player.drawPile.group.iterator() ;

            // Find cards
            CardGroup eligibleCards = new CardGroup(CardGroupType.UNSPECIFIED);
            while ((cardPileIterator != null) && (cardPileIterator.hasNext())) {
                AbstractCard c = (AbstractCard) cardPileIterator.next();
                eligibleCards.addToTop(c);
            }

            if (eligibleCards.isEmpty()) {
                this.isDone = true;
            } else {
                // Prompt selection of card
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
                    addToBot(new ExhaustSpecificCardAction(c, player.drawPile));
                    addToBot(new MakeTempCardInDrawPileAction(cardToReplaceWith, 1, true, true));
                }
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
            }
            this.tickDuration();
        }
    }
}
