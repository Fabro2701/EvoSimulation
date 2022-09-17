package simulator.model.entity;

public interface IInteract {
	public void interact(Entity e);

	public default void myInteract(Entity e) {};

	public default void recieveActiveEntityInteraction(Entity e) {};
	
	public default void recieveActiveEntityReproductionInteraction(Entity e) {};
	public default void recieveActiveEntityAttackInteraction(Entity e) {};
}
