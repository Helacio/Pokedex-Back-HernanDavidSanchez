.PHONY: setup-hooks test-business verify help

# ── Setup ────────────────────────────────────────────────────────────────────

## Instala el pre-commit hook en .git/hooks/
setup-hooks:
	cp .hooks/pre-commit .git/hooks/pre-commit
	chmod +x .git/hooks/pre-commit
	@echo "Hook instalado. Cada commit ejecutará los tests de negocio automáticamente."

# ── Tests ────────────────────────────────────────────────────────────────────

## Corre solo los tests de reglas de negocio (mismo conjunto que el pre-commit hook)
test-business:
	mvn test \
	  -Dtest="PokemonServiceImplTest,AuthServiceImplTest,TeamServiceImplTest,TeamValidatorTest,PokemonStatsTest" \
	  -DfailIfNoSpecifiedTests=false

## Corre el pipeline completo equivalente a CI: tests + JaCoCo + Checkstyle
verify:
	mvn clean verify

# ── Ayuda ────────────────────────────────────────────────────────────────────

help:
	@echo ""
	@echo "Comandos disponibles:"
	@echo "  make setup-hooks    Instala el pre-commit hook de Git"
	@echo "  make test-business  Corre solo los tests de reglas de negocio"
	@echo "  make verify         Corre el pipeline completo (equivalente a CI)"
	@echo ""
