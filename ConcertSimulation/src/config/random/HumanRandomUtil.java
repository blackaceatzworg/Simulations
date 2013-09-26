package config.random;

import model.actors.Sex;

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.joone.edit.jedit.InputHandler.next_char;

import config.Configuration;
import config.HumanConfigurationInfo;

public class HumanRandomUtil {

	private static final NormalDistribution MEN_SIZE_DISTRIBUTION = new NormalDistribution(Configuration.Human.SIZE_MEN_MEAN, Configuration.Human.SIZE_MEN_SD);
	private static final NormalDistribution WOMEN_SIZE_DISTRIBUTION = new NormalDistribution(Configuration.Human.SIZE_WOMEN_MEAN, Configuration.Human.SIZE_WOMEN_SD);
	
	
	private static final NormalDistribution MEN_BMI_DISTRIBUTION = new NormalDistribution(Configuration.Human.BMI_MEN_MEAN,Configuration.Human.BMI_MEN_SD);
	private static final NormalDistribution WOMEN_BMI_DISTRIBUTION = new NormalDistribution(Configuration.Human.BMI_WOMEN_MEAN, Configuration.Human.BMI_WOMEN_SD);
	
	
	
	
	public static HumanConfigurationInfo getRandomHumanConfiguration(char sex ){
		if(Character.toUpperCase(sex)=='M'){
			return HumanRandomUtil.getRandomMaleConfiguration();
		}else if(Character.toUpperCase(sex)=='F'){
			return HumanRandomUtil.getRandomFemaleConfiguration();
		}else{
			throw new IllegalArgumentException("Invalid sex");
		}
	}
	public static HumanConfigurationInfo getRandomMaleConfiguration(){
		HumanConfigurationInfo ci = new HumanConfigurationInfo();
		ci.setSize(getRandomMaleSize());
		ci.setSex(Sex.Male);
		ci.setWeight(getRandomMaleWeight(ci.getSize()));
		
		return ci;
	}
	
	public static HumanConfigurationInfo getRandomFemaleConfiguration(){
		HumanConfigurationInfo ci = new HumanConfigurationInfo();
		ci.setSize(getRandomFemaleSize());
		ci.setWeight(getRandomFemaleWeight(ci.getSize()));
		ci.setSex(Sex.Female);
		return ci;
	}
	
	public static float getRandomMaleSize(){
		return (float) MEN_SIZE_DISTRIBUTION.sample();
	}
	
	public static float getRandomFemaleSize(){
		return (float) WOMEN_SIZE_DISTRIBUTION.sample();
	}
	
	public static float getRandomMaleWeight(float size){
		return (float) Math.sqrt(MEN_BMI_DISTRIBUTION.sample()/Math.pow(size, 2));
	}
	public static float getRandomFemaleWeight(float size){
		return (float) Math.sqrt(WOMEN_BMI_DISTRIBUTION.sample()/Math.pow(size,2));
	}
	
	
	
	
}
