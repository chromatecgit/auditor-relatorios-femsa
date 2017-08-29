package enums;

import utils.ConsolidadosFilter;

public enum ConsolidadoSoviFiltersEnum {
	
	SOVIAGUAFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.AGUA,
					POCTypeEnum.ALL, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVIAGUALOJA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.AGUA,
					POCTypeEnum.ALL, 
					ProductEnum.ALL, 
					CompanyEnum.ALL, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVICHAFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.CHA,
					POCTypeEnum.ALL, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVICHALOJA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.CHA,
					POCTypeEnum.ALL, 
					ProductEnum.ALL, 
					CompanyEnum.ALL, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVICOLASFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.CSD,
					POCTypeEnum.ALL, 
					ProductEnum.COLAS, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVICSDCIFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.CSD,
					POCTypeEnum.ALL, 
					ProductEnum.COLAS, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.SIM, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVICSDFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.CSD,
					POCTypeEnum.ALL, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVICSDLOJA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.CSD,
					POCTypeEnum.ALL, 
					ProductEnum.ALL, 
					CompanyEnum.ALL, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVIENERGETICOFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.ENERGETICO,
					POCTypeEnum.ALL, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVIENERGETICOLOJA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.ENERGETICO,
					POCTypeEnum.ALL, 
					ProductEnum.ALL, 
					CompanyEnum.ALL, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVIFAMRETFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabGroupEnum.ALL.getTabs(),
					POCTypeEnum.ALL, 
					ProductEnum.FAMRET, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVIGDMCSDCIFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.CSD,
					POCTypeEnum.GDM, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.SIM, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVIGDMCSDFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.CSD,
					POCTypeEnum.GDM, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVIGDMCSDLOJA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.CSD,
					POCTypeEnum.GDM, 
					ProductEnum.ALL, 
					CompanyEnum.ALL, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVIGDMNCBCIFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabGroupEnum.NCB.getTabs(),
					POCTypeEnum.GDM, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.SIM, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVIGDMNCBFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabGroupEnum.NCB.getTabs(),
					POCTypeEnum.GDM, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVIGDMNCBLOJA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabGroupEnum.NCB.getTabs(),
					POCTypeEnum.GDM, 
					ProductEnum.ALL, 
					CompanyEnum.ALL, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVIGDMPPCOCSDFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.CSD,
					POCTypeEnum.GDM, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.SIM, 
					ConcorrenciaEnum.SIM)),
	SOVIGDMPPCOCSDLOJA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.CSD,
					POCTypeEnum.GDM, 
					ProductEnum.ALL, 
					CompanyEnum.ALL, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.SIM, 
					ConcorrenciaEnum.SIM)),
	SOVIGDMPPCONCBFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabGroupEnum.NCB.getTabs(),
					POCTypeEnum.GDM, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.SIM, 
					ConcorrenciaEnum.SIM)),
	SOVIGDMPPCONCBLOJA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabGroupEnum.NCB.getTabs(),
					POCTypeEnum.GDM,
					ProductEnum.ALL, 
					CompanyEnum.ALL, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.SIM, 
					ConcorrenciaEnum.SIM)),
	SOVIISOFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.ISOTONICO,
					POCTypeEnum.ALL, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVIISOLOJA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.ISOTONICO,
					POCTypeEnum.ALL, 
					ProductEnum.ALL, 
					CompanyEnum.ALL, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVINCBCIFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabGroupEnum.NCB.getTabs(),
					POCTypeEnum.ALL, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.SIM, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVINCBFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabGroupEnum.NCB.getTabs(),
					POCTypeEnum.ALL, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVINCBLOJA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabGroupEnum.NCB.getTabs(),
					POCTypeEnum.ALL, 
					ProductEnum.ALL, 
					CompanyEnum.ALL, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVISBCSDCIFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.CSD,
					POCTypeEnum.SB, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.SIM, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVISBCSDFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.CSD,
					POCTypeEnum.SB, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVISBCSDLOJA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabEnum.CSD,
					POCTypeEnum.SB, 
					ProductEnum.ALL, 
					CompanyEnum.ALL, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVISUCOFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabGroupEnum.SUCOS.getTabs(),
					POCTypeEnum.ALL, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVISUCOLOJA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabGroupEnum.SUCOS.getTabs(),
					POCTypeEnum.ALL, 
					ProductEnum.ALL, 
					CompanyEnum.ALL, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVISUCOSBCIFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabGroupEnum.SUCOS.getTabs(),
					POCTypeEnum.SB, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.SIM, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVISUCOSBFEMSA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabGroupEnum.SUCOS.getTabs(),
					POCTypeEnum.SB, 
					ProductEnum.ALL, 
					CompanyEnum.FEMSA, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO)),
	SOVISUCOSBLOJA(
			new ConsolidadosFilter(
					ConsolidadoTypeEnum.SOVI,
					TabGroupEnum.SUCOS.getTabs(),
					POCTypeEnum.SB, 
					ProductEnum.ALL, 
					CompanyEnum.ALL, 
					ConsumoImediatoEnum.NAO, 
					PropriaEnum.NAO, 
					ConcorrenciaEnum.NAO));
	
	private ConsolidadosFilter consolidadosFilter;
	
	ConsolidadoSoviFiltersEnum(final ConsolidadosFilter consolidadosFilter) {
		this.consolidadosFilter = consolidadosFilter;
	}

	public ConsolidadosFilter getConsolidadosFilter() {
		return consolidadosFilter;
	}
	
}
