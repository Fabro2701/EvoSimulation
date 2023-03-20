package diagram.code_style;


%%
%type boolean
%line
%column
%unicode


%{
	private CodeStylizer stl;
%}

%eofval{
  return false;
%eofval}

%init{
  stl = new CodeStylizer();
%init}


separador = [ \t\r\b\n]
comentario = @[^\n]* 
if = if 
%%
{separador}               {}
{comentario}              {}
{if}                      {return new UnidadLexica();}
[^]                       {}  