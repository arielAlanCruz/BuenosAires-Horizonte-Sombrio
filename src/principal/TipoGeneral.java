package principal;

public enum TipoGeneral {
	//Efectos de Estado
	ATURDIDO,
	ESCUDO,
	DEBILITADO,
	VENENO,
	
	//Tipos de Habilidad
	HABILIDAD_DANIO,
	HABILIDAD_CURACION,
	HABILIDAD_ESTADO,
	
	//Tipos de Items
	ITEM_CONSUMIBLE,
	ITEM_EQUIPABLE,
	ITEM_MISION,
	
	//Acciones de Combate
	ACCION_ATACAR,
	ACCION_DEFENDER,
	ACCION_HABILIDAD,
	ACCION_ITEM,
	
	//Resultados de Batalla
	VICTORIA,
	DERROTA,
	EN_CURSO
}
