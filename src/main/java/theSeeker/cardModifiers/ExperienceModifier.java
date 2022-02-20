package theSeeker.cardModifiers;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theSeeker.DefaultMod;

public class ExperienceModifier extends AbstractCardModifier {
    public static final String ID = DefaultMod.makeID(ExperienceModifier.class.getSimpleName());
    private int amount = -1;

    public ExperienceModifier(int amount) {
        this.amount = amount;
    }

    public void increaseAmount(int amount) {
        this.amount += amount;
    }

    // Does not actually change anything. Turns out isInherent acts badly with copying.
    @Override
    public boolean isInherent(AbstractCard card) { return false; }

    @Override
    public float modifyDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        return damage + this.amount;
    }

    @Override
    public float modifyBlock(float block, AbstractCard card) {
        return block + this.amount;
    }
    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + this.amount + " theseeker:Experience";
    }

    @Override
    public String identifier(AbstractCard card) {
        return DefaultMod.makeID(ExperienceModifier.class.getSimpleName());
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, this.ID)) {
            ExperienceModifier experienceModifier = (ExperienceModifier) CardModifierManager.getModifiers(card, this.ID).get(0);
            experienceModifier.increaseAmount(this.amount);
            card.initializeDescription();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
    }

    @Override
    public ExperienceModifier makeCopy() {
        return new ExperienceModifier(this.amount);
    }
}
