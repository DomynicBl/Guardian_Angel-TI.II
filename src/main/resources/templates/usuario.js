const lastLogin = 'LastLogin';
const usersDB = 'WAusers';

/*
    LOGIN
*/
let logado = false;
let users;
let ultimoLogin;

function validate() {
    let strDados = localStorage.getItem(lastLogin);
    let objDados = {};

    if (strDados) {
        objDados = JSON.parse(strDados);
        ultimoLogin = objDados.login[0].id;

        if ((Date.now() - objDados.login[0].horario) < 1800000) {
            return true;
        } else {
            return false;
        }
    } else {
        return false;
    }
}

function leUsuarios() {
    let strDados = localStorage.getItem(usersDB);
    let objDados = {};

    if (strDados) {
        objDados = JSON.parse(strDados);
    }
    else {
        objDados = {
            usuarios: [
                { email: "gabriel@gmail.com", nome: "Gabriel", sobrenome: "Quaresma", senha: "Gabriel10" }
            ]
        }
    }

    return objDados;
}

function UserLogado() {
    users = leUsuarios();

    if (!validate()) {
        window.location = "../central.html";
    } else {
        printData();
        exibirDenuncias();
    }

}

function logoff() {
    localStorage.removeItem("LastLogin");
    window.location = "../login/login.html";
}

document.getElementById("btnlogoff").addEventListener("click", logoff);

/*
    DENUNCIAS
*/
const dbUP = 'unsafePoints';

function leDados() {
    // le local storage
    let strDados = localStorage.getItem(dbUP);
    let objDados = {};

    if (strDados) {
        // se ja existirem dados

        // passa os dados para uma variavel
        objDados = JSON.parse(strDados);
    } else {
        // se nao existirem dados

        // cria dados ficticios
        objDados = {
            pontos: [
                {
                    id: 1,
                    userid: 1,
                    rua: "Av. Dom José Gaspar",
                    numero: "500",
                    bairro: "Coração Eucarístico",
                    cidade: "Belo Horizonte",
                    uf: "MG",
                    more: "Assalto",
                    tipo: "Ponto"
                },
                {
                    id: 2,
                    userid: 1,
                    rua: "Av. Princesa Isabel",
                    numero: "1290",
                    bairro: "Copacabana",
                    cidade: "Contagem",
                    uf: "MG",
                    more: "Poste estragado",
                    tipo: "Ponto"
                },
                {
                    id: 3,
                    userid: 1,
                    rua: "R. Bandeiras",
                    numero: "53",
                    bairro: "Sao Joaquim",
                    cidade: "Sao Paulo",
                    uf: "SP",
                    more: "Ermo",
                    tipo: "Local"
                }
            ]
        };
    }

    // retorna as informacoes
    return objDados;
}

function printData() {
    let divdenuncias = document.getElementById('denunciasPontosPerigosos');
    let divnome = document.getElementById('name');
    let divemail = document.getElementById('email');
    let strHTML = '';
    let objDados = leDados();
    let escrito = true;

    let contador = 0;

    for (i = (objDados.pontos.length - 1); (i >= 0) && (contador < 10); i = i - 1) {
        if (objDados.pontos[i].userid == ultimoLogin) {
            strHTML += `<a href="../../Detalhes/moreinfo.html?id=${objDados.pontos[i].id}" onclick='selectInput(this)'><div class="destacado"><h4>${objDados.pontos[i].tipo}</h4><h2>${objDados.pontos[i].rua}, ${objDados.pontos[i].numero} - ${objDados.pontos[i].bairro}, ${objDados.pontos[i].cidade} - ${objDados.pontos[i].uf}</h2><h3>${objDados.pontos[i].more}</h3></div></a>`;
            contador = contador + 1;
            escrito = false;
        }
    }

    if (escrito) {
        strHTML = "Você ainda não possui denúncias";
    }

    divdenuncias.innerHTML = strHTML;
    divnome.innerHTML = `${users.usuarios[ultimoLogin - 1].nome} ${users.usuarios[ultimoLogin - 1].sobrenome}`;
    divemail.innerHTML = `${users.usuarios[ultimoLogin - 1].email}`;
    // UserLogado();
}

/*
    MOTORISTAS
*/

function exibirDenuncias() {
    var denunciasContainer = document.getElementById('denunciasMotoristas');
    var denuncias = leMotoristas();
    denunciasContainer.innerHTML = '';

    let contador = 0;
    for (var i = 0; (i < denuncias.denuncia.length) && (contador < 10); i++) {
        var denuncia = denuncias.denuncia[i];

        if (denuncia.userid == ultimoLogin) {
            var denunciaElement = document.createElement('div');
            denunciaElement.classList.add('denunciasM');

            if (denuncia.tipo == 1) {
                denunciaElement.innerHTML = '<div class="info">Nome do Motorista: ' + denuncia.nome + '</div>' +
                    '<div class="info">Placa: ' + denuncia.placa + '</div>' +
                    '<div class="descricao">' + denuncia.ocorrencia + '</div>';
            } else {
                var horario = new Date(denuncia.horario);
                var dataFormatada = horario.toLocaleDateString();
                var horarioFormatado = horario.toLocaleTimeString();

                denunciaElement.innerHTML = '<div class="info">Nome do Motorista: ' + denuncia.nome + '</div>' +
                    '<div class="info">Linha: ' + denuncia.linha + '</div>' +
                    '<div class="info">Data: ' + dataFormatada + '</div>' +
                    '<div class="info">Horario: ' + horarioFormatado + '</div>' +
                    '<div class="descricao">' + denuncia.ocorrencia + '</div>';
            }

            denunciasContainer.appendChild(denunciaElement);
            contador = contador + 1;
        }
    }
}

function leMotoristas() {
    let strDados = localStorage.getItem('denunciasMotoristas');
    let objDados = {};

    if (strDados) {
        objDados = JSON.parse(strDados);
    } else {
        objDados = {
            denuncia: [
                {
                    nome: 'Walter Rodrigues',
                    userid: 1,
                    tipo: 1,
                    placa: 'DH3J392',
                    ocorrencia: 'Motorista muito mal educado'
                },
                {
                    nome: 'Oscar Franklyn',
                    userid: 2,
                    tipo: 2,
                    linha: '95A',
                    horario: new Date().toISOString(),
                    ocorrencia: 'Direção perigosa'
                }
            ]
        };
        localStorage.setItem('denunciasMotoristas', JSON.stringify(objDados));
    }

    return objDados;
}