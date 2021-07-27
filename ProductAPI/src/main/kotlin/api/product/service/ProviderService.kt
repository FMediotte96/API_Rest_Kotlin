package api.product.service

import api.product.dao.ProviderDAO
import api.product.domain.Provider
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class ProviderService(val providerDAO: ProviderDAO) : BasicCrud<Provider, Int> {
    override fun findAll(): List<Provider> = this.providerDAO.findAll()

    override fun findById(id: Int): Provider? = this.providerDAO.findByIdOrNull(id)

    override fun save(t: Provider): Provider {
        return if (!this.providerDAO.existsById(t.id)) {
            this.providerDAO.save(t)
        } else {
            throw DuplicateKeyException("${t.name} does exists")
        }
    }

    override fun update(t: Provider): Provider {
        return if (this.providerDAO.existsById(t.id)) {
            this.providerDAO.save(t)
        } else {
            throw EntityNotFoundException("${t.name} does not exists")
        }
    }

    override fun deleteById(id: Int): Provider {
        return this.findById(id)?.apply {
            this@ProviderService.providerDAO.deleteById(this.id)
        } ?: throw EntityNotFoundException("$id does not exists")
    }
}