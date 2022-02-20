package theSeeker.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theSeeker.powers.DazzledPower;
import theSeeker.powers.DominionPower;

import java.util.Iterator;

public class BottledSunPotion extends CustomPotion {

    public static final String POTION_ID = theSeeker.DefaultMod.makeID(BottledSunPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public BottledSunPotion() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.FAIRY, PotionColor.ENERGY);
        potency = getPotency();
        description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
        isThrown = false;
        tips.add(new PowerTip(name, description));
        
    }

    @Override
    public void use(AbstractCreature target) {
        AbstractCreature p = AbstractDungeon.player;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            Iterator var4 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
            while(var4.hasNext()) {
                AbstractMonster m2 = (AbstractMonster)var4.next();
                if (!m2.isDeadOrEscaped()) {
                    addToBot(new ApplyPowerAction(m2, p, new DazzledPower(m2, potency), potency, true));
                }
            }
        }
    }
    
    @Override
    public AbstractPotion makeCopy() {
        return new BottledSunPotion();
    }

    @Override
    public int getPotency(final int potency) {
        return 5;
    }

    public void upgradePotion()
    {
      potency += 5;
      tips.clear();
      tips.add(new PowerTip(name, description));
    }
}
