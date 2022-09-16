package br.com.shop.model.enums

enum class State(
    var nameState: String,
    var initials: String
) {
    AMAZONAS("Amazonas", "AM"),
    ALAGOAS("Alagoas", "AL"),
    ACRE("Acre", "AC"),
    AMAPA("Amapá", "AP"),
    BAHIA("Bahia", "BA"),
    PARA("Pará", "PA"),
    MATO_GROSSO("Mato Grosso", "MT"),
    MINAS_GERAIS("Minas Gerais", "MG"),
    MATO_GROSSO_DO_SUL("Mato Grosso do Sul", "MS"),
    GOIAS("Goiás", "GO"),
    MARANHAO("Maranhão", "MA"),
    RIO_GRANDE_DO_SUL("Rio Grande do Sul", "RS"),
    TOCANTINS("Tocantins", "TO"),
    PIAUI("Piauí", "PI"),
    SAO_PAULO("São Paulo", "SP"),
    RONDONIA("Rondônia", "RO"),
    RORAIMA("Roraima", "RR"),
    PARANA("Paraná", "PR"),
    CEARA("Ceará", "CE"),
    PERNAMBUCO("Pernambuco", "PE"),
    SANTA_CATARINA("Santa Catarina", "SC"),
    PARAIBA("Paraíba", "PB"),
    RIO_GRANDE_DO_NORTE("Rio Grande do Norte", "RN"),
    ESPIRITO_SANTO("Espírito Santo", "ES"),
    RIO_DE_JANEIRO("Rio de Janeiro", "RJ"),
    SERGIPE("Sergipe", "SE"),
    DISTRITO_FEDERAL("Distrito Federal", "DF");
}