package com.example.suruchat_app.security

import android.util.Base64
import org.whispersystems.curve25519.Curve25519
import org.whispersystems.curve25519.Curve25519KeyPair
import javax.crypto.spec.SecretKeySpec

class Curve25519Impl {


    companion object{

        private val cipher: Curve25519 = Curve25519.getInstance(Curve25519.BEST)

        fun getKeyPair(): Curve25519KeyPair{

            val keyPair = cipher.generateKeyPair()
//            val keyPairAlice = cipher.generateKeyPair()
//            val sharedSecretAlice = cipher.calculateAgreement(keyPairBob.publicKey,keyPairAlice.privateKey)
//            val sharedSecretBob = cipher.calculateAgreement(keyPairAlice.publicKey,keyPairBob.privateKey)
//            val isSharedEqual = (sharedSecretAlice.contentEquals(sharedSecretBob))
//            println("Hurray they are equal - $isSharedEqual")
//            val secureRandom = SecureRandom.getInstance("")
//            val message = "Hello world".toByteArray()
//            val signature = cipher.calculateSignature(keyPairBob.privateKey, message)
//            val isValid = cipher.verifySignature(keyPairBob.publicKey,message,signature)
//            println("Signature verification - $isValid")

//            val encryptionKey = SecretKeySpec(sharedSecretAlice,0,sharedSecretAlice.size,"AES")
//            val decryptionkey = SecretKeySpec(sharedSecretBob,0,sharedSecretBob.size,"AES")
//            val message = "Hello world"
//            val encryptedBytes = EncryptionDecryption.encryptionMethod(encryptionKey,message)
//            val encryptedString = Base64.encode(encryptedBytes,Base64.DEFAULT)
//            val cipherText = Base64.decode(encryptedString,Base64.DEFAULT)
//            val decryptedMessage = EncryptionDecryption.decryptionMethod(cipherText,decryptionkey)

            return keyPair
        }

        fun encryptMessage(publicKey: ByteArray,privateKey: ByteArray,message: String): String{

            val sharedSecretKey = cipher.calculateAgreement(publicKey, privateKey)

            val encryptionKey = SecretKeySpec(sharedSecretKey,0,sharedSecretKey.size,"AES")

            val encryptedBytes = EncryptionDecryption.encryptionMethod(encryptionKey,message)

            val encryptedString = Base64.encodeToString(encryptedBytes,Base64.DEFAULT)

            return encryptedString
        }

        fun decryptMessage(publicKey: ByteArray,privateKey: ByteArray,receivedMessage: String): String{

            val sharedSecretKey = cipher.calculateAgreement(publicKey, privateKey)

            val decryptionKey = SecretKeySpec(sharedSecretKey,0,sharedSecretKey.size,"AES")

            val cipherText = Base64.decode(receivedMessage,Base64.DEFAULT)

            val message = EncryptionDecryption.decryptionMethod(cipherText,decryptionKey)

            return message
        }
    }
}