package com.example.whatsapp.model;

import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class Conversa {
    private String idRemetente, idDestinatario, ultimaMensagem, isGroup;
    private Usuario usuarioExibicao;
   // private String grupo;
    private Grupo grupo;

    public Conversa() {
        this.setIsGroup("false");
    }
    public void salvar(){
        DatabaseReference database = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference conversaRef = database.child("conversas");
        conversaRef.child(this.getIdRemetente()).child(this.getIdDestinatario()).setValue(this);
    }

    public String getIsGroup() {
        return isGroup;
    }
    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }
    /* public String getGrupo() {
         return grupo;
     }
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }*/
    public String getIdRemetente() {
        return idRemetente;
    }
    public void setIdRemetente(String idRemetente) {
        this.idRemetente = idRemetente;
    }
    public String getIdDestinatario() {
        return idDestinatario;
    }
    public void setIdDestinatario(String idDestinatario) {
        this.idDestinatario = idDestinatario;
    }
    public String getUltimaMensagem() {
        return ultimaMensagem;
    }
    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }
    public Usuario getUsuarioExibicao() {
        return usuarioExibicao;
    }
    public void setUsuarioExibicao(Usuario usuarioExibicao) {
        this.usuarioExibicao = usuarioExibicao;
    }
    public Grupo getGrupo() {
        return grupo;
    }
    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }
}
